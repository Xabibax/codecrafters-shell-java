package executable;

import app.Context;
import app.ast.SimpleCommand;
import app.token.word.Word;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static app.Context.FAIL;
import static app.Context.IO_FAIL;

public record Executable(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand simpleCommand) {

        final var pbCommand = simpleCommand.parameters().stream().map(Word::value).collect(Collectors.toList());
        pbCommand.addFirst(simpleCommand.command().value());

        final var pb = new ProcessBuilder(pbCommand);
        pb.redirectErrorStream(true);

        try {
            final var process = pb.start();
            final var exitValue = process.waitFor();
            process.getInputStream().transferTo(System.out);

            return exitValue;

        } catch (Exception e) {
            return handleExecutableException(e);
        }
    }

    private int handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> FAIL;
        };
    }
}
