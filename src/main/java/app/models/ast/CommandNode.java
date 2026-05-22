package app.models.ast;

import app.AppContext;
import app.models.ast.command.Type;
import app.models.result.Result;
import app.models.token.word.Word;
import app.models.token.word.Words;
import org.jspecify.annotations.NonNull;

import static app.models.ast.command.Type.EXECUTABLE;
import static app.models.ast.command.Type.NOT_FOUND;

public record CommandNode(Word command, Words parameters) implements AST {

    public CommandNode(String literal, Words parameters) {
        this(Word.of(literal), parameters);
    }

    public CommandNode(Word command) {
        this(command, Words.of());
    }

    @Override
    public Result apply(AppContext appContext) {
        Type type = Type.getTypeFrom(command());
        return switch (type) {
            case BLANK, EXIT, TYPE, ECHO, PWD, EXECUTABLE, CD -> type.apply(appContext, this);
            case NOT_FOUND -> appContext.handleExecutableSearch(command())
                    .map(_ -> EXECUTABLE)
                    .orElse(NOT_FOUND)
                    .apply(appContext, this)
            ;
        };
    }

    @NonNull
    public String toString() {
        return command().value() + " " + parameters();
    }
}
