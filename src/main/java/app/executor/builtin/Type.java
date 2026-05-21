package app.executor.builtin;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.Token;

import java.util.Objects;
import java.util.function.Function;

import static app.models.ast.command.Type.*;

public record Type(AppContext appContext) implements Function<CommandNode, Result> {
    @Override
    public Result apply(CommandNode commandNode) {
        final var parameter = commandNode.parameters()
                .isEmpty() ? Token.builder()
                             .build() : commandNode.parameters()
                                        .getFirst();
        final var message = type(parameter);

        return new ResultDefault(message, Result.SUCCESS);
    }

    private String type(Token token) {
        var command = getTypeFrom(token);
        if (NOT_FOUND.equals(command)) {
            command = appContext().handleExecutableSearch(token)
                    .isPresent() ? EXECUTABLE : NOT_FOUND;
        }

        return handleCommand(token, command);
    }

    private String handleCommand(Token token, app.models.ast.command.Type type) {
        return switch (type) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(token.value()) + ": not found";
            case EXECUTABLE -> typeExecutable(token);
            default -> type.name()
                    .toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(Token token) {
        return appContext().handleExecutableSearch(token)
                .map(f -> token.value() + " is " + f.getAbsolutePath())
                .orElse(handleCommand(token, NOT_FOUND));
    }
}
