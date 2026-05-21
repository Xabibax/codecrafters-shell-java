package app.models.token;


import app.models.token.operator.OperatorDefault;
import app.models.token.wordpart.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import static app.models.token.Type.OPERATOR;

@Getter
@Setter
public class TokenBuilder {
    private StringBuilder value = new StringBuilder();
    private WordParts wordParts = new WordParts();
    @Setter(AccessLevel.PRIVATE)
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

    public TokenBuilder setSingleQuoteState() {
        this.state = State.SINGLE_QUOTED;
        return this;
    }

    public TokenBuilder setDoubleQuoteState() {
        this.state = State.DOUBLE_QUOTED;
        return this;
    }

    public TokenBuilder setNormalState() {
        this.state = State.NORMAL;
        return this;
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value.toString(), state);
    }

    public void reset() {
        value("");
        wordParts().clear();
        setState(State.NORMAL);
        type(Type.WORD);
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return switch (state) {
            case NORMAL -> value.toString()
                    .isBlank();
            case SINGLE_QUOTED, DOUBLE_QUOTED -> value.isEmpty();
        };
    }

    public void appendWordPart() {
        if (OPERATOR.equals(type)) {
            throw new IllegalStateException("Cannot appendWordPart with an operator");
        }
        wordParts().add(getWordPart());
        value("").setState(State.NORMAL);
        type(Type.WORD);
    }

    public Token build() {
        return switch (type) {
            case WORD -> getWord();
            case OPERATOR -> getOperator();
        };
    }

    private @NonNull Word getWord() {
        wordParts().add(getWordPart());
        return new WordDefault(wordParts());
    }

    private @NonNull WordPart getWordPart() {
        return switch (state) {
            case NORMAL -> new Literal(value);
            case SINGLE_QUOTED -> new SingleQuoted(value);
            case DOUBLE_QUOTED -> new DoubleQuoted(value);
        };
    }

    private @NonNull Operator getOperator() {
        return new OperatorDefault(value);
    }

}
