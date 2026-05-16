package app.parser;

import app.ast.AST;
import app.lexer.token.Tokens;

import java.util.function.Function;

public interface Parser extends Function<Tokens, AST> {
    AST apply(Tokens tokens);
}
