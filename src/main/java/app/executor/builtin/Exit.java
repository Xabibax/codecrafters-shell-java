package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;

import java.util.function.Function;

import static app.executor.ResultDefault.SUCCESS;

public record Exit(AppContext appContext) implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        System.exit(Result.SUCCESS);
        return SUCCESS;
    }
}
