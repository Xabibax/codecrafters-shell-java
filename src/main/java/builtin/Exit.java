package builtin;

import app.Context;

import java.util.function.Function;

public record Exit(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {
        System.exit(Context.SUCCESS);
        return Context.SUCCESS;
    }
}
