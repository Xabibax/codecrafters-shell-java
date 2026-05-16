package app.ast.command;

import app.ast.CommandNode;
import app.lexer.token.Token;
import app.lexer.token.Tokens;
import app.lexer.token.word.Word;
import app.lexer.token.word.Words;

public class CommandFactory {

    private final static CommandFactory instance = new CommandFactory();

    public static CommandFactory getInstance() {
        return instance;
    }

    public CommandNode getCommand(Tokens tokens) {
        Word command = (Word) tokens.getFirst();
        Words parameters = tokens.stream().skip(1).map(Word.class::cast).collect(Words.toList()).trim();
        return new CommandNode(command, parameters);
    }

    public CommandNode getCommand(Token token) {
        return new CommandNode((Word) token);
    }
}
