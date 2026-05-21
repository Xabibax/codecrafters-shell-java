package app.parser;

import app.models.ast.AST;
import app.models.token.Tokens;

import java.util.function.Function;

public interface Parser extends Function<Tokens, AST> {
    AST apply(Tokens tokens);
}
