package app.lexer.token.word;

import app.lexer.token.Token;

public class Word extends Token {
    public Word(String value, State state) {
        super(value, state);
    }

    public Word(StringBuilder value, State state) {
        this(value.toString(), state);
    }
}
