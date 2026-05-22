package app.models.token.operator;

import app.models.token.Token;
import org.jspecify.annotations.NonNull;

public sealed interface Operator extends Token permits Output {
    static @NonNull Operator of(StringBuilder value) {
        return of(value.toString());
    }

    static @NonNull Operator of(String value) {
        return switch (value) {
            case ">" -> new Output();
            default -> throw new IllegalArgumentException("No operator found for %s".formatted(value));
        };
    }
}
