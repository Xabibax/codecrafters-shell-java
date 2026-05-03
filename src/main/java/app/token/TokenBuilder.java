package app.token;


import app.token.word.Word;

public class TokenBuilder {
    private StringBuilder value = new StringBuilder();
    private Token.State state = Token.State.NORMAL;
    private Type type = Type.WORD;

    TokenBuilder() {
    }

    public TokenBuilder value(String value) {
        this.value = new StringBuilder(value);
        return this;
    }

    public TokenBuilder append(char value) {
        return append(String.valueOf(value));
    }

    public TokenBuilder append(String value) {
        this.value.append(value);
        return this;
    }

    public TokenBuilder state(Token.State state) {
        this.state = state;
        return this;
    }

    public TokenBuilder type(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value.toString(), state);
    }

    public TokenBuilder reset() {
        value("");
        state(Token.State.NORMAL);
        return this;
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return switch (state) {
            case NORMAL -> value.toString().isBlank();
            case SINGLE_QUOTED, DOUBLE_QUOTED -> value.isEmpty();
            case SPACE -> true;
        };
    }

    public Token build() {
        return switch (type) {
            case WORD -> new Word(value, state);
            case OPERATOR -> new Operator(value, state);
        };
    }

}
