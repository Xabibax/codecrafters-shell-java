package builtin;

import app.Context;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import static builtin.Command.*;

public record Type(Context context) implements Function<String, Integer> {
    @Override
    public Integer apply(String line) {
        final var parameters = Arrays.stream(line.split(" ")).skip(1).toList();

        boolean emptyParameters = Objects.requireNonNull(parameters, "parameters shouldn't be null").isEmpty();

        final var commandLabel = emptyParameters ? "" : parameters.getFirst();
        final var message = type(commandLabel);
        IO.println(message);

        return Context.SUCCESS;
    }


    private String type(String commandLabel) {
        var command = getCommandFrom(commandLabel);
        if (NOT_FOUND.equals(command)) {
            command = context().handleExecutableSearch(commandLabel).isPresent() ? EXECUTABLE : NOT_FOUND;
        }

        return handleCommand(commandLabel, command);
    }

    private String handleCommand(String commandLabel, Command command) {
        return switch (command) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(commandLabel) + ": not found";
            case EXECUTABLE -> typeExecutable(commandLabel);
            default -> command.name().toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(String commandLabel) {
        return context().handleExecutableSearch(commandLabel)
                .map(f -> commandLabel + " is " + f.getAbsolutePath())
                .orElse(handleCommand(commandLabel, NOT_FOUND));
    }
}
