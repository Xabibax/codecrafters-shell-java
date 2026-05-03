package builtin;

import app.Context;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public record Pwd(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {
        IO.println(context.getCurrentDirectory());

        return Context.SUCCESS;
    }
}
