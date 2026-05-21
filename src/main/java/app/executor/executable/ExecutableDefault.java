package app.executor.executable;

import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.Word;
import app.models.token.Words;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static app.models.result.ResultDefault.IO_FAIL;


public record ExecutableDefault() implements Executable {

    @Override
    public Result apply(CommandNode commandNode) {

        final var commands = merger(commandNode.parameters());

        commands.addFirst(commandNode.command()
                .value());

        final var pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);

        try {
            final var process = pb.start();
            final var exitValue = process.waitFor();
            process.getInputStream()
                    .transferTo(System.out);

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
