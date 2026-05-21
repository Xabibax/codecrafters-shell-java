package app.executor.builtin;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.util.function.Function;

public record Pwd(AppContext appContext) implements Function<CommandNode, Result> {

    @Override
    public Result apply(CommandNode commandNode) {
        final var currentDirectory = appContext.getCurrentDirectory()
                .toString();

        return ResultDefault.success(currentDirectory);
    }
}
