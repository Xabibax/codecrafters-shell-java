package app;

import app.lexer.token.Token;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AppContext {
    public static final String PATH = "PATH";
    public static final String HOME = "HOME";
    public static final String USER_DIR = "user.dir";
    public static final String USER_HOME = "user.home";
    public final Factory factory = new Factory();

    private Path currentDirectory;

    public AppContext() {
        this(Paths.get(System.getProperty(USER_DIR)));
    }

    public AppContext(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public Path getHomeDirectory() {
        String home = System.getenv(HOME);
        String userHome = System.getProperty(USER_HOME);
        return Paths.get(home != null ? home : userHome);
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrendDirectory(Path newDirectory) {
        this.currentDirectory = newDirectory;
    }

    private List<String> getPaths() {
        return Arrays.stream(System.getenv(PATH).split(":")).toList();
    }

    public Optional<File> handleExecutableSearch(Token token) {
        final var paths = getPaths();

        return paths.stream()
                .map(path -> Paths.get(path, token.value()).toFile())
                .filter(File::isFile)
                .filter(File::canExecute)
                .findAny();
    }

}
