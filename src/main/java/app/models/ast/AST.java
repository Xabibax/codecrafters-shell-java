package app.models.ast;

import app.AppContext;
import app.models.result.Result;

import java.util.function.Function;

public sealed interface AST extends Function<AppContext, Result> permits CommandNode, RedirectNode {
    Result apply(AppContext appContext);
}