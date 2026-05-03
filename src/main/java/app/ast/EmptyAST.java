package app.ast;

import app.Context;

public record EmptyAST() implements AST {
    @Override
    public Integer apply(Context context) {
        return 0;
    }
}
