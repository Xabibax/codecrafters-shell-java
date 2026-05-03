package builtin;

import app.Context;

import java.util.function.BiFunction;

public enum Command implements BiFunction<Context, String, Integer> {
    NOT_FOUND, BLANK, EXIT, ECHO, TYPE, EXECUTABLE(false), PWD, CD,
    ;

    final boolean builtIn;

    Command() {
        this(true);
    }

    Command(boolean isBuiltIn) {
        this.builtIn = isBuiltIn;
    }

    public static Command getCommandFrom(String command) {
        if (command.isBlank()) {
            return BLANK;
        }

        try {
            return Command.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }

    }


    @Override
    public Integer apply(Context context, String line) {
        return switch (this) {
            case NOT_FOUND -> printCommandNotFound(line);
            case EXIT -> context.exit().apply(line);
            case BLANK -> Context.WARNING;
            case ECHO -> context.echo().apply(line);
            case TYPE -> context.type().apply(line);
            case EXECUTABLE -> context.executable().apply(line);
            case PWD -> context.pwd().apply(line);
            case CD -> context.cd().apply(line);
        };
    }

    private Integer printCommandNotFound(String command) {
        IO.print("%s: command not found%n".formatted(command));

        return Context.WARNING;
    }
}
