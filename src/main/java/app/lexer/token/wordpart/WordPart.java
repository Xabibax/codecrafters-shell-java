package app.lexer.token.wordpart;

import app.lexer.token.State;

public sealed interface WordPart permits DoubleQuoted, Escaped, Literal, SingleQuoted {
    static WordPart getWordPartFrom(String value, State state) {
        return switch (state) {
            case NORMAL -> new Literal(value);
            case SINGLE_QUOTED -> new SingleQuoted(value);
            case DOUBLE_QUOTED -> new DoubleQuoted(value);
        };
    }

    String value();
}
