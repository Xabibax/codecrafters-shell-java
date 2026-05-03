package app.ast;

import app.Context;
import app.token.Token;

import java.util.function.BiFunction;

public enum Type implements BiFunction<Context, SimpleCommand, Integer> {
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
            return app.ast.Type.valueOf(token.value().toUpperCase());
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }
    }

    @Override
    public Integer apply(Context context, SimpleCommand command) {
        return switch (this) {
            case NOT_FOUND -> printCommandNotFound(command);
            case EXIT -> context.exit().apply(command);
            case BLANK -> Context.WARNING;
            case ECHO -> context.echo().apply(command);
            case TYPE -> context.type().apply(command);
            case EXECUTABLE -> context.executable().apply(command);
            case PWD -> context.pwd().apply(command);
            case CD -> context.cd().apply(command);
        };
    }

    private Integer printCommandNotFound(SimpleCommand command) {

        IO.print("%s: command not found%n".formatted(command.command().value()));

        return Context.WARNING;
    }
}
