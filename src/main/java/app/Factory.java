package app;

import app.executor.ExecutorDefault;
import app.executor.builtin.*;
import app.executor.executable.ExecutableDefault;
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
    private ExecutableDefault executable;

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

    public ExecutorDefault executor(AppContext appContext) {
        if (executor == null) {
            executor = new ExecutorDefault(appContext);
        }
        return executor;
    }

    public ExecutableDefault executable() {
        if (executable == null) {
            executable = new ExecutableDefault();
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

    public Echo echo() {
        if (echo == null) {
            echo = new Echo();
        }
        return echo;
    }

    public Cd cd(AppContext appContext) {
        if (cd == null) {
            cd = new Cd(appContext);
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