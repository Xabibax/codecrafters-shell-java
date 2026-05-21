package app.models.ast.command;

import app.models.ast.CommandNode;
import app.models.token.Word;
import app.models.token.WordDefault;
import app.models.token.Words;
import lombok.Getter;

public class CommandFactory {

    @Getter
    private final static CommandFactory instance = new CommandFactory();

    public CommandNode getCommand(Word word) {
        return getCommand(word, Words.of());
    }

    public CommandNode getCommand(Word word, Words parameters) {
        return new CommandNode(word, parameters);
    }

    public CommandNode getCommand(Words words) {
        Word command = words.getFirst();
        Words parameters = words.stream()
                .skip(1)
                .map(WordDefault.class::cast)
                .collect(Words.toList())
                ;
        return new CommandNode(command, parameters);
    }

}
