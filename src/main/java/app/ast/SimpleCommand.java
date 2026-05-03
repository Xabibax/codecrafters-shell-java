package app.ast;

import app.Context;
import app.token.word.Word;
import app.token.word.Words;

import static app.ast.Type.EXECUTABLE;
import static app.ast.Type.NOT_FOUND;

public record SimpleCommand(Word command, Words parameters) implements AST {

    public SimpleCommand(Word command) {
        this(command, Words.of());
    }

    @Override
    public Integer apply(Context context) {
        Type type = Type.getTypeFrom(command());
        return switch (type) {
            case BLANK, EXIT, TYPE, ECHO, PWD, EXECUTABLE, CD -> type.apply(context, this);
            case NOT_FOUND ->
                    context.handleExecutableSearch(command()).map(f -> EXECUTABLE).orElse(NOT_FOUND).apply(context, this);
        };
    }

    public String toString() {
        return command().value() + " " + parameters();
    }
}
