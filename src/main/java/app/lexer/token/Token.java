package app.lexer.token;

public abstract class Token {

    private final String value;
    private final State state;

    protected Token(String value, State state) {
        this.value = value;
        this.state = state;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public String value() {
        return value;
    }

    public State state() {
        return state;
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value, state);
    }

    public enum State {
        NORMAL(true),
        SINGLE_QUOTED(true),
        DOUBLE_QUOTED(true),
        SPACE(true),
        ;

        final boolean isMergeable;

        State(boolean isMergeable) {
            this.isMergeable = isMergeable;
        }

        public boolean isMergeable() {
            return isMergeable;
        }
    }
}
