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

    final var tokens = appContext.getFactory()
            .lexer()
            .apply(input)
            ;

    final var ast = appContext.getFactory()
            .parser()
            .apply(tokens)
            ;

    final var executionResult = appContext.getFactory()
            .executor(appContext)
            .apply(ast)
            ;

    if (executionResult.getOutput()
            .lastIndexOf("\n") == executionResult.getOutput()
            .length() - 1 || executionResult.getOutput()
            .lastIndexOf("\r") == executionResult.getOutput()
            .length() - 1) {
        IO.print(executionResult.getOutput());
    } else {
        IO.println(executionResult.getOutput());
    }
}

private void printPrompt() {
    IO.print(SHELL_PROMPT);
}
