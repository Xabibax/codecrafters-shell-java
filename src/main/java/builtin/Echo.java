package builtin;

import app.Context;

import java.util.Arrays;
import java.util.function.Function;

public record Echo(Context context) implements Function<String, Integer> {
    @Override
    public Integer apply(String line) {
        final var parameters = Arrays.stream(line.split(" ")).skip(1).toList();
        String message = String.join(" ", parameters);
        IO.println(message);
        return Context.SUCCESS;
    }
}
