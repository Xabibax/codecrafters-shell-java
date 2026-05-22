package app.models.token.word.wordpart;


import org.jspecify.annotations.NonNull;

public record Escaped(String value) implements WordPart {
    public Escaped(StringBuilder value) {
        this(value.toString());
    }

    @NonNull
    public String toString() {
        return "\\%s".formatted(value());
    }
}
