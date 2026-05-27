package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;

import static app.models.result.ResultDefault.SUCCESS;

public record Exit() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {
        System.exit(Result.SUCCESS);
        return SUCCESS;
    }
}
