package app.executor;

import app.Context;
import app.ast.AST;

import java.util.function.Function;

public record Executor(Context appContext) implements Function<AST, Integer> {
    @Override
    public Integer apply(AST ast) {
        return ast.apply(appContext());
    }
}
