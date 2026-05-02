import jdk.jshell.spi.ExecutionControl;

public static final String PATH = "PATH";

void main() {
    while (true) {
        printPrompt();
        final var line = IO.readln();


        final var splitLine = line.split(" ");

        final var command = Command.getCommandFrom(splitLine[0]);
        final var parameters = Arrays.stream(splitLine).skip(1).toList();

        if (!command.builtIn) {
            final var res = handleExecutable(splitLine);
            continue;
        }

        switch (command) {
            case NOT_FOUND -> printCommandNotFound(line);
            case EXIT -> exit(parameters);
            case BLANK -> {
            }
            case ECHO -> echo(parameters);
            case TYPE -> type(parameters);
        }
    }
}

private int handleExecutable(String[] splitLine) {
    final var paths = getPaths();

    final var executable = splitLine[0];
    final var parameters = Arrays.stream(splitLine).skip(1).collect(Collectors.joining(" "));

    throw new RuntimeException("handleExecutable not yet implemented");
}

private static List<String> getPaths() {
    return Arrays.stream(System.getenv(PATH).split(":")).toList();
}

private static void echo(List<String> parameters) {
    String message = String.join(" ", parameters);
    IO.println(message);
}

private static void exit(List<String> parameters) {
    System.exit(0);
}

private static void type(List<String> parameters) {
    if (Objects.requireNonNull(parameters, "parameters shouldn't be null").isEmpty()) {
        IO.println(Command.BLANK.type(""));
        return;
    }
    var command = Command.getCommandFrom(parameters.getFirst());

    final var message = command.type(parameters.getFirst());
    IO.println(message);
}

private static Optional<File> handleExecutableSearch(String command) {
    final var paths = getPaths();

    return paths.stream()
            .map(path -> Path.of(path, command).toFile())
            .filter(File::isFile)
            .filter(File::canExecute)
            .findAny();
}

private static void printCommandNotFound(String command) {
    IO.print("%s: command not found%n".formatted(command));
}

private static void printPrompt() {
    IO.print("$ ");
}

enum Command {
    NOT_FOUND, BLANK, EXIT, ECHO, TYPE, EXECUTABLE(false),
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
            return handleExecutableSearch(command).isPresent() ? EXECUTABLE : NOT_FOUND;
        }

    }

    public String type(String command) {
        return switch (this) {
            case BLANK, NOT_FOUND -> Objects.requireNonNull(command) + ": not found";
            case EXECUTABLE -> typeExecutable(command);
            default -> name().toLowerCase() + " is a shell builtin";
        };
    }

    private String typeExecutable(String command) {
        return handleExecutableSearch(command)
                .map(f -> command + " is " + f.getAbsolutePath())
                .orElse(Command.NOT_FOUND.type(command));
    }
}
