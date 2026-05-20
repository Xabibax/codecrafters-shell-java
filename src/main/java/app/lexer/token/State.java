package app.lexer.token;

public enum State {
    NORMAL(true),
    SINGLE_QUOTED(true),
    DOUBLE_QUOTED(true),
    ;

    final boolean isMergeable;

    State(boolean isMergeable) {
        this.isMergeable = isMergeable;
    }

    public boolean isMergeable() {
        return isMergeable;
    }
}
