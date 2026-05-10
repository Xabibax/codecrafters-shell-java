package app.ast;

import app.token.Token;
import app.token.Tokens;
import app.token.word.Word;
import app.token.word.Words;

public class CommandFactory {

    private final static CommandFactory instance = new CommandFactory();

    public static CommandFactory getInstance() {
        return instance;
    }

    public SimpleCommand getCommand(Tokens tokens) {
        Word command = (Word) tokens.getFirst();
        Words parameters = tokens.stream().skip(1).map(Word.class::cast).collect(Words.toList()).trim();
        return new SimpleCommand(command, parameters);
    }

    public SimpleCommand getCommand(Token token) {
        return new SimpleCommand((Word) token);
    }
}
