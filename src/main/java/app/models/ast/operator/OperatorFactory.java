package app.models.ast.operator;

import app.models.ast.CommandNode;
import app.models.token.Token;
import app.models.token.Tokens;
import app.models.token.WordDefault;
import app.models.token.Words;
import lombok.Getter;

public class OperatorFactory {

    @Getter
    private final static OperatorFactory instance = new OperatorFactory();

    public CommandNode getCommand(Tokens tokens) {
        WordDefault command = (WordDefault) tokens.getFirst();
        Words parameters = tokens.stream()
                .skip(1)
                .map(WordDefault.class::cast)
                .collect(Words.toList())
                ;
        return new CommandNode(command, parameters);
    }

    public CommandNode getCommand(Token token) {
        return new CommandNode((WordDefault) token);
    }
}
