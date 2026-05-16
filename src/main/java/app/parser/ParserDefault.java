package app.parser;

import app.AppContext;
import app.ast.AST;
import app.ast.command.CommandFactory;
import app.lexer.token.Tokens;

public record ParserDefault(AppContext appContext) implements Parser {

    @Override
    public AST apply(Tokens tokens) {
        CommandFactory commandFactory = CommandFactory.getInstance();
        if (tokens.size() == 1) {
            return commandFactory.getCommand(tokens.getFirst());
        }
        return commandFactory.getCommand(tokens);
    }
}
