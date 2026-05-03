package app.builtin;

import app.Context;
import app.ast.SimpleCommand;

import java.util.function.Function;

public record Exit(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand command) {
        System.exit(Context.SUCCESS);
        return Context.SUCCESS;
    }
}
