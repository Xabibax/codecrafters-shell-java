package app.models.token.operator;

import app.models.token.Token;
import org.jspecify.annotations.NonNull;

public sealed interface Operator extends Token permits RedirectErr, RedirectOut {
    static @NonNull Operator of(StringBuilder value) {
        return of(value.toString());
    }

    static @NonNull Operator of(String value) {
        return switch (value) {
            case ">", "1>", ">>", "1>>" -> new RedirectOut(value);
            case "2>", "2>>" -> new RedirectErr(value);
            default -> throw new IllegalArgumentException("No operator found for %s".formatted(value));
        };
    }
}
