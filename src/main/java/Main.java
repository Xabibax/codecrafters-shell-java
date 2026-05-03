import app.Context;

public static final String SHELL_PROMPT = "$ ";

@SuppressWarnings("InfiniteLoopStatement")
void main() {
    Context context = new Context();
    do {
        printPrompt();
        handleInput(context);
    } while (true);
}

private void handleInput(Context context) {
    final var input = IO.readln();

    final var tokens = context.lexer().apply(input);

    final var ast = context.parser().apply(tokens);

    final var executionResult = context.executor().apply(ast);
}

private void printPrompt() {
    IO.print(SHELL_PROMPT);
}
