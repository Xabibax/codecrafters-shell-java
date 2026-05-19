import app.AppContext;

public static final String SHELL_PROMPT = "$ ";

@SuppressWarnings("InfiniteLoopStatement")
void main() {
    AppContext appContext = new AppContext();
    do {
        printPrompt();
        handleInput(appContext);
    } while (true);
}

private void handleInput(AppContext appContext) {
    final var input = IO.readln();

    final var tokens = appContext.factory.lexer(appContext).apply(input);

    final var ast = appContext.factory.parser(appContext).apply(tokens);

    final var executionResult = appContext.factory.executor(appContext).apply(ast);

    IO.println(executionResult.getOutput());
}

private void printPrompt() {
    IO.print(SHELL_PROMPT);
}
