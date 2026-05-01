void main() {
    while (true) {
        printPrompt();
        final var scanner = new Scanner(System.in);
        final var line = scanner.nextLine();

        final var command = Command.getCommandFrom(line);
        final var parameters = Arrays.stream(line.split(" ")).skip(1).toList();


        switch (command) {
            case ECHO -> echo(parameters);
            case EXIT -> {
                return;
            }
            case NOT_FOUND -> printCommandNotFound(line);
            case BLANK -> {}
        }
    }
}

private static void echo(List<String> parameters) {
    String message = String.join(" ", parameters);
    System.out.println(message);
}

private static void printCommandNotFound(String command) {
    System.out.printf("%s: command not found%n", command);
}

private static void printPrompt() {
    IO.print("$ ");
}

enum Command {
    BLANK,
    ECHO,
    EXIT,
    NOT_FOUND,
    ;

    public static Command getCommandFrom(String line) {
        final var command = line.split(" ")[0];
        if(command.isBlank()) {
            return BLANK;
        }

        return switch (command) {
            case "exit" -> EXIT;
            case "echo" -> ECHO;
            default -> NOT_FOUND;
        };

    }
}