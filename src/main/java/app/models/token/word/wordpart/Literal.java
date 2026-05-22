package app.models.token.word.wordpart;


import org.jspecify.annotations.NonNull;

public record Literal(String value) implements WordPart {
    public Literal(StringBuilder value) {
        this(value.toString());
    }

    @NonNull
    public String toString() {
        return value();
    }
}
