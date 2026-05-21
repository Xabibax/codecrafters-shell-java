package app.models.token.wordpart;


import org.jspecify.annotations.NonNull;

public record SingleQuoted(String value) implements WordPart {
    public SingleQuoted(StringBuilder value) {
        this(value.toString());
    }

    @NonNull
    public String toString() {
        return "'%s'".formatted(value());
    }
}
