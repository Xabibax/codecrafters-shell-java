package app.lexer.token;


import app.lexer.token.operator.OperatorDefault;
import app.lexer.token.wordpart.WordParts;

public class TokenBuilder {
    private StringBuilder value = new StringBuilder();
    private WordParts wordParts = new WordParts();
    private State state = State.NORMAL;
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

    public TokenBuilder state(State state) {
        this.state = state;
        return this;
    }

    public TokenBuilder type(Type type) {
        this.type = type;
        return this;
    }

    public WordParts wordParts() {
        return wordParts;
    }

    public TokenBuilder setWordParts(WordParts wordParts) {
        this.wordParts = wordParts;
        return this;
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value.toString(), state);
    }

    public TokenBuilder reset() {
        value("");
        state(State.NORMAL);
        type(Type.WORD);
        return this;
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return switch (state) {
            case NORMAL -> value.toString().isBlank();
            case SINGLE_QUOTED, DOUBLE_QUOTED -> value.isEmpty();
        };
    }

    public Token build() {
        return switch (type) {
            case WORD -> new WordDefault(value, state);
            case OPERATOR -> new OperatorDefault(value, state);
        };
    }

}
