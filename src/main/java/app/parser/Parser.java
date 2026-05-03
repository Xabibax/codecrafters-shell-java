package app.parser;

import app.Context;
import app.ast.AST;
import app.ast.CommandFactory;
import app.ast.EmptyAST;
import app.token.Tokens;

import java.util.function.Function;

public record Parser(Context appContext) implements Function<Tokens, AST> {

    public AST apply(Tokens tokens) {
        if (tokens.isEmpty()) {
            return new EmptyAST();
        }
        CommandFactory commandFactory = CommandFactory.getInstance();
        if (tokens.size() == 1) {
            return commandFactory.getCommand(tokens.getFirst());
        }
        return commandFactory.getCommand(tokens);
    }
}
