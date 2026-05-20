package app.ast.operator;

import app.ast.CommandNode;
import app.lexer.token.Token;
import app.lexer.token.Tokens;
import app.lexer.token.WordDefault;
import app.lexer.token.Words;

public class OperatorFactory {

    private final static OperatorFactory instance = new OperatorFactory();

    public static OperatorFactory getInstance() {
        return instance;
    }

    public CommandNode getCommand(Tokens tokens) {
        WordDefault command = (WordDefault) tokens.getFirst();
        Words parameters = tokens.stream().skip(1).map(WordDefault.class::cast).collect(Words.toList()).trim();
        return new CommandNode(command, parameters);
    }

    public CommandNode getCommand(Token token) {
        return new CommandNode((WordDefault) token);
    }
}
