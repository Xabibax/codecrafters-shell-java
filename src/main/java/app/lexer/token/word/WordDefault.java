package app.lexer.token.word;

import app.lexer.token.State;
import app.lexer.token.Word;

public record WordDefault(String value, State state) implements Word {

    public WordDefault(StringBuilder value, State state) {
        this(value.toString(), state);
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value, state);
    }
}
