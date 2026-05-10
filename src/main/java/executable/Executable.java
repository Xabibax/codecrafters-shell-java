package executable;

import app.Context;
import app.ast.SimpleCommand;
import app.token.Token;
import app.token.word.Words;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static app.Context.FAIL;
import static app.Context.IO_FAIL;

public record Executable(Context context) implements Function<SimpleCommand, Integer> {

    @Override
    public Integer apply(SimpleCommand simpleCommand) {

        final var commands = merger(simpleCommand.parameters());

        commands.addFirst(simpleCommand.command().value());

        final var pb = new ProcessBuilder(commands);
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


    private List<String> merger(Words words) {
        final List<String> res = new ArrayList<>();
        int size = words.size();
        for (int i = 0; i < size; i++) {
            final var parameter = words.get(i);
            if (i == 0) {
                res.add(parameter.value());
                continue;
            }
            final var previousParameter = words.get(i - 1);
            switch (parameter.state()) {
                case Token.State.NORMAL -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SPACE -> res.add(parameter.value());
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> res.set(res.size() - 1, res.getLast() + parameter.value());
                    }
                }
                case Token.State.SINGLE_QUOTED, Token.State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED ->
                                res.set(res.size() - 1, res.getLast() + parameter.value());
                        case SPACE -> res.add(parameter.value());
                    }
                }
                case Token.State.SPACE -> {
                }
            }
        }
        return res;
    }

    private int handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> FAIL;
        };
    }
}
