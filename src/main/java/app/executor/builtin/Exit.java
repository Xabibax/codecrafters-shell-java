package app.executor.builtin;

import app.models.ast.CommandNode;
import app.models.result.Result;

import java.util.function.Function;

import static app.models.result.ResultDefault.SUCCESS;

public record Exit() implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        System.exit(Result.SUCCESS);
        return SUCCESS;
    }
}
