package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;
import app.executor.ResultDefault;

import java.util.function.Function;

public record Pwd(AppContext appContext) implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        final var currentDirectory = appContext.getCurrentDirectory().toString();

        return ResultDefault.success(currentDirectory);
    }
}
