package app;

import app.models.token.Token;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class AppContext {
    private final Factory factory;
    private Path currentDirectory;

    private InputStream stdin;
    private PrintStream stdout;
    private PrintStream stderr;

    private BufferedReader br;

    public AppContext() {
        this(new Factory());
    }

    public AppContext(Factory factory) {
        this.factory = factory;
        this.currentDirectory = Paths.get(System.getProperty("user.dir"));
        this.stdin = System.in;
        this.stdout = System.out;
        this.stderr = System.err;
    }

    public Path getHomeDirectory() {
        String home = System.getenv("HOME");
        String userHome = System.getProperty("user.home");
        return Paths.get(home != null ? home : userHome);
    }

    private List<String> getPaths() {
        return Arrays.stream(System.getenv("PATH")
                        .split(":"))
                .toList();
    }

    public Optional<File> handleExecutableSearch(Token token) {
        final var paths = getPaths();

        return paths.stream()
                .map(path -> Paths.get(path, token.value())
                        .toFile())
                .filter(File::isFile)
                .filter(File::canExecute)
                .findAny();
    }

    private synchronized BufferedReader reader() {
        if (br == null) {
            String enc = System.getProperty("stdin.encoding", "");
            Charset cs = Charset.forName(enc, StandardCharsets.UTF_8);
            br = new BufferedReader(new InputStreamReader(getStdin(), cs));
        }
        return br;
    }

    public String readln() {
        try {
            return reader().readLine();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public void IOReset() {
        this.stdin = System.in;
        getStdout().flush();
        this.stdout = System.out;
        getStderr().flush();
        this.stderr = System.err;
    }
}
