void main() {
    while (true) {
        printPrompt();
        final var scanner = new Scanner(System.in);
        final var command = scanner.nextLine();

        if (command.isBlank()) {
            continue;
        }

        switch (command) {
            default -> printCommandNotFound(command);
        }
    }
}

private static void printCommandNotFound(String command) {
    System.out.printf("%s: command not found%n", command);
}

private static void printPrompt() {
    IO.print("$ ");
}
