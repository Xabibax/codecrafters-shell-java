package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;
import app.executor.ResultDefault;
import app.lexer.token.Tokens;

import java.util.function.Function;

public record Echo(AppContext appContext) implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        String message = commandNode.parameters().stream().collect(Tokens.joining());

        return ResultDefault.success(message);
    }
}
