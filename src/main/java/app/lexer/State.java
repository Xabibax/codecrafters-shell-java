package app.lexer;

enum State {
    NORMAL, SINGLE_QUOTES_OPEN(false), DOUBLE_QUOTES_OPEN(false), SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_CLOSE, SPACE(false);

    final boolean isTerminal;

    State() {
        this.isTerminal = true;
    }

    State(boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    boolean isTerminal() {
        return this.isTerminal;
    }
}
