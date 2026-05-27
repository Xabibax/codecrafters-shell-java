package app;

import app.executor.ExecutorDefault;
import app.executor.executable.ExecutableDefault;
import app.executor.executable.ExecutableNotFound;
import app.executor.executable.builtin.*;
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
    private ExecutableDefault executableDefault;
    private ExecutableNotFound executableNotFound;

    public ParserDefault parser() {
        if (parser == null) {
            parser = new ParserDefault();
        }
        return parser;
    }

    public LexerDefault lexer() {
        if (lexer == null) {
            lexer = new LexerDefault();
        }
        return lexer;
    }

    public ExecutorDefault executor() {
        if (executor == null) {
            executor = new ExecutorDefault();
        }
        return executor;
    }

    public ExecutableDefault executable() {
        if (executableDefault == null) {
            executableDefault = new ExecutableDefault();
        }
        return executableDefault;
    }

    public ExecutableNotFound executableNotFound() {
        if (executableNotFound == null) {
            executableNotFound = new ExecutableNotFound();
        }
        return executableNotFound;
    }

    public Type type() {
        if (type == null) {
            type = new Type();
        }
        return type;
    }

    public Pwd pwd() {
        if (pwd == null) {
            pwd = new Pwd();
        }
        return pwd;
    }

    public Echo echo() {
        if (echo == null) {
            echo = new Echo();
        }
        return echo;
    }

    public Cd cd() {
        if (cd == null) {
            cd = new Cd();
        }
        return cd;
    }

    public Exit exit() {
        if (exit == null) {
            exit = new Exit();
        }
        return exit;
    }
}