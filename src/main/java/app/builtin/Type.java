package app.builtin;

import app.Context;
import app.ast.SimpleCommand;
import app.token.Token;

import java.util.Objects;
import java.util.function.Function;

import static app.ast.Type.*;

public record Type(Context context) implements Function<SimpleCommand, Integer> {
    @Override
    public Integer apply(SimpleCommand command) {
        final var parameter = command.parameters().isEmpty() ? Token.builder().build() : command.parameters().getFirst();
        final var message = type(parameter);
        IO.println(message);

        return Context.SUCCESS;
    }

    private String type(Token token) {
        var command = getTypeFrom(token);
        if (NOT_FOUND.equals(command)) {
            command = context().handleExecutableSearch(token).isPresent() ? EXECUTABLE : NOT_FOUND;
        }

        return handleCommand(token, command);
    }

    private String handleCommand(Token token, app.ast.Type type) {
        return switch (type) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(token.value()) + ": not found";
            case EXECUTABLE -> typeExecutable(token);
            default -> type.name().toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(Token token) {
        return context().handleExecutableSearch(token)
                .map(f -> token.value() + " is " + f.getAbsolutePath())
                .orElse(handleCommand(token, NOT_FOUND));
    }
}
