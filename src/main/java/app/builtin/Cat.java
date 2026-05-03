package app.builtin;

import app.Context;

import java.util.function.Function;

public record Cat(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {
        System.err.println("Not yet implemented");
        return Context.FAIL;
    }
}
