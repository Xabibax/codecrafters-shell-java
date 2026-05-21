package app.executor;

import app.AppContext;
import app.models.ast.AST;
import app.models.result.Result;

public record ExecutorDefault(AppContext appContext) implements Executor {
    @Override
    public Result apply(AST ast) {
        return ast.apply(appContext());
    }
}
