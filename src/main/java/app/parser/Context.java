package app.parser;

import app.ast.AST;
import app.ast.command.CommandFactory;
import app.ast.operator.OperatorFactory;
import app.lexer.token.Tokens;
import app.lexer.token.Words;


class Context {
    private final Tokens tokens;
    private final Words currWords;
    private final CommandFactory commandFactory;
    private final OperatorFactory operatorFactory;
    private AST ast = null;


    Context(Tokens tokens) {
        this.tokens = tokens;
        currWords = new Words();
        commandFactory = CommandFactory.getInstance();
        operatorFactory = OperatorFactory.getInstance();
    }

    public CommandFactory commandFactory() {
        return commandFactory;
    }

    public OperatorFactory operatorFactory() {
        return operatorFactory;
    }

    public Words currWords() {
        return currWords;
    }

    public AST ast() {
        return ast;
    }

    public Context ast(AST ast) {
        this.ast = ast;
        return this;
    }

    public Tokens tokens() {
        return tokens;
    }
}
