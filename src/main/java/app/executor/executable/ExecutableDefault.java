package app.executor.executable;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.word.Word;
import app.models.token.word.Words;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public record ExecutableDefault() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {

        final var commands = merger(commandNode.parameters());
        commands.addFirst(commandNode.command().value());

        final var pb = new ProcessBuilder(commands);
        pb.directory(appContext.getCurrentDirectory().toFile());

        try {
            final var process = pb.start();
            process.getInputStream()
                    .transferTo(appContext.getStdout());
            process.getErrorStream()
                    .transferTo(appContext.getStderr());
            final var exitValue = process.waitFor();
            return exitValue == 0 ? ResultDefault.SUCCESS : new ResultDefault("", exitValue);

        } catch (Exception e) {
            return ResultDefault.handleExecutableException(e);
        }
    }

    private List<String> merger(Words words) {
        return words.stream()
                .map(Word::toString)
                .collect(Collectors.toList());
    }

}
