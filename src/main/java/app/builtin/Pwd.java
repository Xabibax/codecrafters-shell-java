package app.builtin;

import app.Context;
import app.ast.SimpleCommand;

import java.util.function.Function;

public record Pwd(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand command) {
        IO.println(context.getCurrentDirectory());

        return Context.SUCCESS;
    }
}
