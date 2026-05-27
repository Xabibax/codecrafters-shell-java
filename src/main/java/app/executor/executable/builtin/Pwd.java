package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public record Pwd() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {
        final var currentDirectory = appContext.getCurrentDirectory()
                .toString();

        appContext.getStdout().println(currentDirectory);

        return ResultDefault.SUCCESS;
    }
}
