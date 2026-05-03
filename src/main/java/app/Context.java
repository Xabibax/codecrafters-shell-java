package app;

import app.builtin.*;
import app.executor.Executor;
import app.lexer.Lexer;
import app.parser.Parser;
import app.token.Token;
import executable.Executable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Context {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int WARNING = 2;
    public static final int IO_FAIL = 3;
    public static final String PATH = "PATH";
    public static final String HOME = "HOME";
    public static final String USER_DIR = "user.dir";
    public static final String USER_HOME = "user.home";

    private Lexer lexer;
    private Parser parser;
    private Executor executor;

    private Type type;
    private Path currentDirectory;


    private Cd cd;
    private Exit exit;
    private Pwd pwd;
    private Echo echo;
    private Executable executable;

    public Context() {
        this(Paths.get(System.getProperty(USER_DIR)));
    }

    public Context(Path currentDirectory) {
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

    public Executor executor() {
        if (this.executor == null) {
            this.executor = new Executor(this);
        }
        return executor;
    }

    public Parser parser() {
        if (this.parser == null) {
            this.parser = new Parser(this);
        }
        return this.parser;
    }

    public Lexer lexer() {
        if (this.lexer == null) {
            this.lexer = new Lexer(this);
        }
        return this.lexer;
    }

    public Type type() {
        if (this.type == null) {
            this.type = new Type(this);
        }
        return this.type;
    }

    public Cd cd() {
        if (this.cd == null) {
            this.cd = new Cd(this);
        }
        return this.cd;
    }

    public Exit exit() {
        if (this.exit == null) {
            this.exit = new Exit(this);
        }
        return this.exit;
    }

    public Pwd pwd() {
        if (this.pwd == null) {
            this.pwd = new Pwd(this);
        }
        return this.pwd;
    }

    public Echo echo() {
        if (this.echo == null) {
            this.echo = new Echo(this);
        }
        return this.echo;
    }

    public Executable executable() {
        if (this.executable == null) {
            this.executable = new Executable(this);
        }
        return this.executable;
    }
}
