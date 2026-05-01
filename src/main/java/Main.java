import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.print("$ ");
            final var scanner = new Scanner(System.in);
            final var command = scanner.nextLine();

            switch (command) {
                default -> System.out.printf("%s: command not found%n", command);
            }
        }
    }
}
