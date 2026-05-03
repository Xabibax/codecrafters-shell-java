package builtin;

import app.Context;

import java.util.function.Function;

public record Pwd(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {
        IO.println(context.getCurrentDirectory());

        return Context.SUCCESS;
    }
}
