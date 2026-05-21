package app.parser;

import app.models.ast.AST;
import app.models.ast.command.CommandFactory;
import app.models.ast.operator.OperatorFactory;
import app.models.token.Tokens;
import app.models.token.Words;
import lombok.Getter;
import lombok.Setter;


@Getter()
class Context {
    private final Tokens tokens;
    private final Words currWords;
    private final CommandFactory commandFactory;
    private final OperatorFactory operatorFactory;
    @Setter
    private AST ast = null;


    Context(Tokens tokens) {
        this.tokens = tokens;
        currWords = new Words();
        commandFactory = CommandFactory.getInstance();
        operatorFactory = OperatorFactory.getInstance();
    }

    public Words currWords() {
        return currWords;
    }

    public Tokens tokens() {
        return tokens;
    }
}
