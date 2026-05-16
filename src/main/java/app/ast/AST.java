package app.ast;

import app.AppContext;

public sealed interface AST permits CommandNode {
    Integer apply(AppContext appContext);
}