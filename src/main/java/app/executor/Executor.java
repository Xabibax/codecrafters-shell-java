package app.executor;

import app.AppContext;
import app.models.ast.AST;
import app.models.result.Result;

import java.util.function.BiFunction;

public interface Executor extends BiFunction<AST, AppContext, Result> {
    @Override
    Result apply(AST ast, AppContext context);
}
