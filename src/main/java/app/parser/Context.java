package app.parser;

import app.ast.AST;
import app.ast.command.CommandFactory;
import app.ast.operator.OperatorFactory;
import app.lexer.token.word.Words;


class Context {
    private final Words words;
    private final CommandFactory commandFactory;
    private final OperatorFactory operatorFactory;
    private AST ast = null;


    Context() {
        words = new Words();
        commandFactory = CommandFactory.getInstance();
        operatorFactory = OperatorFactory.getInstance();
    }

    public CommandFactory commandFactory() {
        return commandFactory;
    }

    public OperatorFactory operatorFactory() {
        return operatorFactory;
    }

    public Words words() {
        return words;
    }

    public AST ast() {
        return ast;
    }

    public Context ast(AST ast) {
        this.ast = ast;
        return this;
    }
}
