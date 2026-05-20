package app.executor.executable;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;
import app.executor.ResultDefault;
import app.lexer.token.Word;
import app.lexer.token.Words;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static app.executor.ResultDefault.IO_FAIL;


public record ExecutableDefault(AppContext appContext) implements Executable {

    private static void appendToken(List<String> res, Word parameter) {
        final var origin = res.getLast();
        final var toAppend = parameter.value();
        res.set(res.size() - 1, origin + toAppend);
    }

    @Override
    public Result apply(CommandNode commandNode) {

        final var commands = merger(commandNode.parameters());

        commands.addFirst(commandNode.command().value());

        final var pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);

        try {
            final var process = pb.start();
            final var exitValue = process.waitFor();
            process.getInputStream().transferTo(System.out);

            return new ResultDefault("", exitValue);

        } catch (Exception e) {
            return handleExecutableException(e);
        }
    }

    private List<String> merger(Words words) {
        return words.stream().map(Word::toString).collect(Collectors.toList());
    }

    private Result handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> ResultDefault.FAIL;
        };
    }
}
