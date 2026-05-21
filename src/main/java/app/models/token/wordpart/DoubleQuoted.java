package app.models.token.wordpart;


import org.jspecify.annotations.NonNull;

public record DoubleQuoted(String value) implements WordPart {
    public DoubleQuoted(StringBuilder value) {
        this(value.toString());
    }

    @NonNull
    public String toString() {
        return "\"%s\"".formatted(value());
    }
}
