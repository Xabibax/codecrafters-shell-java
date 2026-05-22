import app.AppContext;
import app.models.result.Result;

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

    switch (executionResult.getCode()) {
        case Result.SUCCESS, Result.WARNING -> handleOutput(executionResult.getOutput());
        case Result.FAIL, Result.IO_FAIL -> handleOutput(executionResult.getErrorOutput());
    }
}

private static void handleOutput(String output) {
    if (output
            .lastIndexOf("\n") == output
            .length() - 1 || output
            .lastIndexOf("\r") == output
            .length() - 1) {
        IO.print(output);
    } else {
        IO.println(output);
    }
}

private void printPrompt() {
    IO.print(SHELL_PROMPT);
}
