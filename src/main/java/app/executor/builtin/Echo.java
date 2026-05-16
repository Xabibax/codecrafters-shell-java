package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;
import app.lexer.token.Tokens;

import java.util.function.Function;

public record Echo(AppContext appContext) implements Function<CommandNode, Integer> {

    @Override
    public Integer apply(CommandNode commandNode) {
        String message = commandNode.parameters().stream().collect(Tokens.joining());

        IO.println(message);

        return AppContext.SUCCESS;
    }
}
