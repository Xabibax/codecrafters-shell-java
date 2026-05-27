package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.io.IOException;

public record Echo() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {
        String message = commandNode.parameters()
                .toString();

        appContext.getStdout().println(message);

        return ResultDefault.SUCCESS;
    }
}
