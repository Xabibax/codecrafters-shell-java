package app.models.ast;

import app.AppContext;
import app.models.result.Result;

import java.util.List;

public record RedirectNode(AST ast, List<Redirect> redirects) implements AST {

    public Result apply(AppContext appContext) {
        return ast().apply(appContext);
    }
}
