package builtin;

import app.Context;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record Exit(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {
        final var splitLine = Arrays.stream(line.split(" ")).toList();

        final var command = splitLine.getFirst();
        final var parameters = splitLine.stream().skip(1).toList();
        System.exit(Context.SUCCESS);
        return Context.SUCCESS;
    }
}
