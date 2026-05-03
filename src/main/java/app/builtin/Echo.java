package app.builtin;

import app.Context;
import app.ast.SimpleCommand;
import app.token.Token;

import java.util.function.Function;
import java.util.stream.Collectors;

public record Echo(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand command) {
        String message = command.parameters().stream()
                .map(Token::value).collect(Collectors.joining(" "));

        IO.println(message);

        return Context.SUCCESS;
    }
}
