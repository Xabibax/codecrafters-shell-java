package app.ast.command;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;
import app.executor.ResultDefault;
import app.lexer.token.Token;

import java.util.function.BiFunction;

public enum Type implements BiFunction<AppContext, CommandNode, Result> {
    NOT_FOUND, BLANK, EXIT, ECHO, TYPE, EXECUTABLE(false), PWD, CD,
    ;

    final boolean builtIn;

    Type() {
        this(true);
    }

    Type(boolean isBuiltIn) {
        this.builtIn = isBuiltIn;
    }

    public static Type getTypeFrom(Token token) {
        if (token == null || token.value().isBlank()) {
            return BLANK;
        }

        try {
            return Type.valueOf(token.value().toUpperCase());
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }
    }

    private static Result handleBlank() {
        return ResultDefault.WARNING;
    }

    @Override
    public Result apply(AppContext appContext, CommandNode commandNode) {
        return switch (this) {
            case NOT_FOUND -> handleCommandNotFound(commandNode);
            case EXIT -> appContext.factory.exit(appContext).apply(commandNode);
            case BLANK -> handleBlank();
            case ECHO -> appContext.factory.echo(appContext).apply(commandNode);
            case TYPE -> appContext.factory.type(appContext).apply(commandNode);
            case EXECUTABLE -> appContext.factory.executable(appContext).apply(commandNode);
            case PWD -> appContext.factory.pwd(appContext).apply(commandNode);
            case CD -> appContext.factory.cd(appContext).apply(commandNode);
        };
    }

    private Result handleCommandNotFound(CommandNode commandNode) {
        String commandNotFound = "%s: command not found%n".formatted(commandNode.command().value());

        return ResultDefault.warning(commandNotFound);
    }
}
