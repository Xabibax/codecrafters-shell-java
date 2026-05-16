package app.executor;

import app.ast.AST;

import java.util.function.Function;

public interface Executor extends Function<AST, Integer> {
    @Override
    Integer apply(AST ast);
}
