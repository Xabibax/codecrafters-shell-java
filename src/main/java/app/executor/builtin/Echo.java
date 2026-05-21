package app.executor.builtin;

import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.util.function.Function;

public record Echo() implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        String message = commandNode.parameters()
                .toString();

        return ResultDefault.success(message);
    }
}
