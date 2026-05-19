package app.executor;

import app.ast.AST;

import java.util.function.Function;

public interface Executor extends Function<AST, Result> {
    @Override
    Result apply(AST ast);
}
