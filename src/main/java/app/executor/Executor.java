package app.executor;

import app.models.ast.AST;
import app.models.result.Result;

import java.util.function.Function;

public interface Executor extends Function<AST, Result> {
    @Override
    Result apply(AST ast);
}
