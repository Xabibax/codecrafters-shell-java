package app;

import app.executor.builtin.*;
import app.executor.executable.Executable;
import app.executor.ExecutorDefault;
import app.lexer.LexerDefault;
import app.parser.ParserDefault;

public class Factory {
    private LexerDefault lexer;
    private ParserDefault parser;
    private ExecutorDefault executor;
    private Type type;
    private Cd cd;
    private Exit exit;
    private Pwd pwd;
    private Echo echo;
    private Executable executable;


    public Factory() {
    }

    public ParserDefault parser(AppContext appContext) {
        if (parser == null) {
            parser = new ParserDefault(appContext);
        }
        return parser;
    }

    public LexerDefault lexer(AppContext appContext) {
        if (lexer == null) {
            lexer = new LexerDefault(appContext);
        }
        return lexer;
    }

    public ExecutorDefault executor(AppContext appContext) {
        if (executor == null) {
            executor = new ExecutorDefault(appContext);
        }
        return executor;
    }

    public Executable executable(AppContext appContext) {
        if (executable == null) {
            executable = new Executable(appContext);
        }
        return executable;
    }

    public Type type(AppContext appContext) {
        if (type == null) {
            type = new Type(appContext);
        }
        return type;
    }

    public Pwd pwd(AppContext appContext) {
        if (pwd == null) {
            pwd = new Pwd(appContext);
        }
        return pwd;
    }

    public Echo echo(AppContext appContext) {
        if (echo == null) {
            echo = new Echo(appContext);
        }
        return echo;
    }

    public Cd cd(AppContext appContext) {
        if (cd == null) {
            cd = new Cd(appContext);
        }
        return cd;
    }

    public Exit exit(AppContext appContext) {
        if (exit == null) {
            exit = new Exit(appContext);
        }
        return exit;
    }
}