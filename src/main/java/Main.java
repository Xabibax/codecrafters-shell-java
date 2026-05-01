void main() {
    while (true) {
        printPrompt();
        final var scanner = new Scanner(System.in);
        final var line = scanner.nextLine();


        final var splitLine = line.split(" ");

        final var command = Command.getCommandFrom(splitLine[0]);
        final var parameters = Arrays.stream(splitLine).skip(1).toList();


        switch (command) {
            case NOT_FOUND -> printCommandNotFound(line);
            case EXIT -> {
                return;
            }
            case BLANK -> {}
            case ECHO -> echo(parameters);
            case TYPE -> type(parameters);
        }
    }
}

private static void echo(List<String> parameters) {
    String message = String.join(" ", parameters);
    System.out.println(message);
}

private static void type(List<String> parameters) {
    final var command = Command.getCommandFrom(parameters.getFirst());
    final var message = command.type().orElse(parameters.getFirst() + ": not found");
    System.out.println(message);
}

private static void printCommandNotFound(String command) {
    System.out.printf("%s: command not found%n", command);
}

private static void printPrompt() {
    IO.print("$ ");
}

enum Command {
    NOT_FOUND,
    BLANK,
    EXIT,
    ECHO,
    TYPE,
    ;

    public static Command getCommandFrom(String line) {
        final var command = line.trim().split(" ")[0].toUpperCase();
        if(command.isBlank()) {
            return BLANK;
        }

        try {
            return Command.valueOf(command);
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }

    }

    public Optional<String> type() {
        return switch (this) {
            case BLANK, NOT_FOUND -> Optional.empty();
            default -> Optional.of(name().toLowerCase() + " is a shell builtin");
        };
    }
}