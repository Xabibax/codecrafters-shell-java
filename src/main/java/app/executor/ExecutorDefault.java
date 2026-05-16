package app.executor;

import app.AppContext;
import app.ast.AST;

public record ExecutorDefault(AppContext appContext) implements Executor {
    @Override
    public Integer apply(AST ast) {
        return ast.apply(appContext());
    }
}
