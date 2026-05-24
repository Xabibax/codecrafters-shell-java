package app.executor.executable;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.word.Word;
import app.models.token.word.Words;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static app.models.result.ResultDefault.IO_FAIL;


public record ExecutableDefault() implements Executable {

    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {

        final var commands = merger(commandNode.parameters());
        final var commandPath = appContext.handleExecutableSearch(commandNode.command())
                .orElseThrow()
                .getAbsolutePath()
                ;

        commands.addFirst(commandPath);

        final var pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);

        try {
            final var process = pb.start();
            process.getInputStream()
                    .transferTo(appContext.getStdout());
            process.getErrorStream()
                    .transferTo(appContext.getStderr());
            final var exitValue = process.waitFor();

            return new ResultDefault("", exitValue);

        } catch (Exception e) {
            return handleExecutableException(e);
        }
    }

    private List<String> merger(Words words) {
        return words.stream()
                .map(Word::toString)
                .collect(Collectors.toList());
    }

    private Result handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> ResultDefault.FAIL;
        };
    }
}
