package app.executor.executable;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;


public record ExecutableNotFound() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {

        String message = "%s: command not found".formatted(commandNode.command()
                .value());

        appContext.getStdout()
                .println(message);

        return ResultDefault.fail(message);
    }
}
