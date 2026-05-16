package app.lexer;

public enum LexerState {
    NORMAL, SINGLE_QUOTES_OPEN(false), DOUBLE_QUOTES_OPEN(false), SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_CLOSE, SPACE,
    ;

    final boolean isTerminal;

    LexerState() {
        this.isTerminal = true;
    }

    LexerState(boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    boolean isTerminal() {
        return this.isTerminal;
    }
}
