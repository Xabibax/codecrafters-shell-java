package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.util.function.Function;

public record Pwd() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {
        final var currentDirectory = appContext.getCurrentDirectory()
                .toString();

        return ResultDefault.success(currentDirectory);
    }
}
