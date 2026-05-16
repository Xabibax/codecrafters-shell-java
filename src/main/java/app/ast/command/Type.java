package app.ast.command;

import app.AppContext;
import app.ast.CommandNode;
import app.lexer.token.Token;

import java.util.function.BiFunction;

public enum Type implements BiFunction<AppContext, CommandNode, Integer> {
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

    @Override
    public Integer apply(AppContext appContext, CommandNode commandNode) {
        return switch (this) {
            case NOT_FOUND -> printCommandNotFound(commandNode);
            case EXIT -> appContext.factory.exit(appContext).apply(commandNode);
            case BLANK -> AppContext.WARNING;
            case ECHO -> appContext.factory.echo(appContext).apply(commandNode);
            case TYPE -> appContext.factory.type(appContext).apply(commandNode);
            case EXECUTABLE -> appContext.factory.executable(appContext).apply(commandNode);
            case PWD -> appContext.factory.pwd(appContext).apply(commandNode);
            case CD -> appContext.factory.cd(appContext).apply(commandNode);
        };
    }

    private Integer printCommandNotFound(CommandNode commandNode) {

        IO.print("%s: command not found%n".formatted(commandNode.command().value()));

        return AppContext.WARNING;
    }
}
