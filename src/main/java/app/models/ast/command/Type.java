package app.models.ast.command;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.Token;

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
        if (token == null || token.value()
                .isBlank()) {
            return BLANK;
        }

        try {
            return Type.valueOf(token.value()
                    .toUpperCase());
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
            case EXIT -> appContext.getFactory()
                    .exit()
                    .apply(commandNode)
            ;
            case BLANK -> handleBlank();
            case ECHO -> appContext.getFactory()
                    .echo()
                    .apply(commandNode)
            ;
            case TYPE -> appContext.getFactory()
                    .type(appContext)
                    .apply(commandNode)
            ;
            case EXECUTABLE -> appContext.getFactory()
                    .executable()
                    .apply(commandNode)
            ;
            case PWD -> appContext.getFactory()
                    .pwd(appContext)
                    .apply(commandNode)
            ;
            case CD -> appContext.getFactory()
                    .cd(appContext)
                    .apply(commandNode)
            ;
        };
    }

    private Result handleCommandNotFound(CommandNode commandNode) {
        String commandNotFound = "%s: command not found".formatted(commandNode.command()
                .value());

        return ResultDefault.warning(commandNotFound);
    }
}
