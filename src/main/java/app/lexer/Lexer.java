package app.lexer;

import app.lexer.token.Tokens;

import java.util.function.Function;

public interface Lexer extends Function<String, Tokens> {
    Tokens apply(String input) throws IncoherentFinalStateException;
}
