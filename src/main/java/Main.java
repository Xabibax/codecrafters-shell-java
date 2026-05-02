import jdk.jshell.spi.ExecutionControl;

public static final String PATH = "PATH";

void main() {
    while (true) {
        printPrompt();
        final var line = IO.readln();


        final var splitLine = Arrays.stream(line.split(" ")).toList();

        final var command = splitLine.getFirst();
        final var parameters = splitLine.stream().skip(1).toList();

        switch (Command.getCommandFrom(command)) {
            case NOT_FOUND -> printCommandNotFound(line);
            case EXIT -> exit(parameters);
            case BLANK -> {
            }
            case ECHO -> echo(parameters);
            case TYPE -> type(parameters);
            case EXECUTABLE -> handleExecutable(splitLine);
        }
    }
}

private int handleExecutable(List<String> splitLine) {
    final var paths = getPaths();

    final var pb = new ProcessBuilder(splitLine);
    pb.redirectErrorStream(true);

    try {
        final var process = pb.start();
        final var exitValue = process.waitFor();
        process.getInputStream().transferTo(System.out);

        return exitValue;

    } catch (Exception e) {
        return handleExecutableException(e);
    }
}

private int handleExecutableException(Exception e) {
    return switch (e) {
        case IOException _ -> 2;
        default -> 1;
    };
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
