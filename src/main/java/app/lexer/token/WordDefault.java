package app.lexer.token;

import app.lexer.token.wordpart.WordPart;
import app.lexer.token.wordpart.WordParts;

public record WordDefault(WordParts wordParts, State state) implements Word {

    public WordDefault(String value, State state) {
        this(WordParts.of(WordPart.getWordPartFrom(value, state)), state);
    }

    public WordDefault(StringBuilder value, State state) {
        this(value.toString(), state);
    }

    @Override
    public String value() {
        return wordParts().toString();
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(wordParts(), state);
    }
}
