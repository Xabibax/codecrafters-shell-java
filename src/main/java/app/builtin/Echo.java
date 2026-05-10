package app.builtin;

import app.Context;
import app.ast.SimpleCommand;
import app.token.Tokens;

import java.util.function.Function;

public record Echo(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand command) {
        String message = command.parameters().stream().collect(Tokens.joining());

        IO.println(message);

        return Context.SUCCESS;
    }
}
