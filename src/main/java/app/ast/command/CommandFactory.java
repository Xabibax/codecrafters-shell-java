package app.ast.command;

import app.ast.CommandNode;
import app.lexer.token.Token;
import app.lexer.token.Word;
import app.lexer.token.WordDefault;
import app.lexer.token.Words;

public class CommandFactory {

    private final static CommandFactory instance = new CommandFactory();

    public static CommandFactory getInstance() {
        return instance;
    }

    public CommandNode getCommand(Words words) {
        Word command = words.getFirst();
        Words parameters = words.stream().skip(1).map(WordDefault.class::cast).collect(Words.toList()).trim();
        return new CommandNode(command, parameters);
    }

    public CommandNode getCommand(Token token) {
        return new CommandNode((WordDefault) token);
    }
}
