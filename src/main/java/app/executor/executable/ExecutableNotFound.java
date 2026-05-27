package app.executor.executable;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.word.Word;
import app.models.token.word.Words;

import java.util.List;
import java.util.stream.Collectors;


public record ExecutableNotFound() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {

        String message = "%s: command not found".formatted(commandNode.command()
                .value());

        appContext.getStdout().println(message);

        return ResultDefault.fail(message);
    }

    private List<String> merger(Words words) {
        return words.stream()
                .map(Word::toString)
                .collect(Collectors.toList());
    }
}
