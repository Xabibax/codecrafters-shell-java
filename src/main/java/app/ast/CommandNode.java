package app.ast;

import app.AppContext;
import app.ast.command.Type;
import app.executor.Result;
import app.lexer.token.Word;
import app.lexer.token.Words;

import static app.ast.command.Type.EXECUTABLE;
import static app.ast.command.Type.NOT_FOUND;

public record CommandNode(Word command, Words parameters) implements AST {

    public CommandNode {
        parameters.trim();
    }

    public CommandNode(Word command) {
        this(command, Words.of());
    }

    @Override
    public Result apply(AppContext appContext) {
        Type type = Type.getTypeFrom(command());
        return switch (type) {
            case BLANK, EXIT, TYPE, ECHO, PWD, EXECUTABLE, CD -> type.apply(appContext, this);
            case NOT_FOUND ->
                    appContext.handleExecutableSearch(command()).map(_ -> EXECUTABLE).orElse(NOT_FOUND).apply(appContext, this);
        };
    }

    public String toString() {
        return command().value() + " " + parameters();
    }
}
