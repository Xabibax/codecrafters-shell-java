import app.AppContext;

public static final String SHELL_PROMPT = "$ ";

@SuppressWarnings("InfiniteLoopStatement")
void main() throws IOException {
    AppContext appContext = new AppContext();
    do {
        printPrompt();
        handleInput(appContext);
        appContext.IOReset();
    } while (true);
}

private void handleInput(AppContext appContext) {
    final var input = appContext.readln();

    final var tokens = appContext.getFactory()
            .lexer()
            .apply(input)
            ;

    final var ast = appContext.getFactory()
            .parser()
            .apply(tokens)
            ;

    final var executionResult = appContext.getFactory()
            .executor()
            .apply(ast, appContext)
            ;

}

private void printPrompt() {
    IO.print(SHELL_PROMPT);
}
