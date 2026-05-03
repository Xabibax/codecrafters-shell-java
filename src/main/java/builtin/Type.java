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

        final var message = emptyParameters ? "" : parameters.getFirst();
        IO.println(message);

        return Context.SUCCESS;
    }


    private String type(String commandLabel) {
        var command = getCommandFrom(commandLabel);
        if (NOT_FOUND.equals(command)) {
            command = context().handleExecutableSearch(commandLabel).isPresent() ? EXECUTABLE : NOT_FOUND;
        }

        return switch (command) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(command) + ": not found";
            case EXECUTABLE -> typeExecutable(commandLabel);
            default -> command.name().toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(String command) {
        return context().handleExecutableSearch(command).map(f -> command + " is " + f.getAbsolutePath()).orElse(type(command));
    }
}
