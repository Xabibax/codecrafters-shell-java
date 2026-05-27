package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.Token;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import static app.models.ast.command.Type.*;

public record Type() implements Executable {
    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {
        final var parameter = commandNode.parameters()
                .isEmpty() ? Token.builder()
                .build() : commandNode.parameters()
                .getFirst();
        final var message = type(parameter, appContext);

        appContext.getStdout().println(message);

        return ResultDefault.SUCCESS;
    }

    private String type(Token token, AppContext appContext) {
        var command = getTypeFrom(token);
        if (NOT_FOUND.equals(command)) {
            command = appContext.handleExecutableSearch(token)
                    .isPresent() ? EXECUTABLE : NOT_FOUND;
        }

        return handleCommand(token, command, appContext);
    }

    private String handleCommand(Token token, app.models.ast.command.Type type, AppContext appContext) {
        return switch (type) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(token.value()) + ": not found";
            case EXECUTABLE -> typeExecutable(token, appContext);
            default -> type.name()
                    .toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(Token token, AppContext appContext) {
        return appContext.handleExecutableSearch(token)
                .map(f -> token.value() + " is " + f.getAbsolutePath())
                .orElse(handleCommand(token, NOT_FOUND, appContext));
    }
}
