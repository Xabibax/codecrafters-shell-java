package app.executor.executable;

import app.AppContext;
import app.ast.CommandNode;
import app.executor.Result;
import app.executor.ResultDefault;
import app.lexer.token.State;
import app.lexer.token.Word;
import app.lexer.token.Words;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                case State.NORMAL -> {
                    switch (previousParameter.state()) {
                        case NORMAL -> res.add(parameter.value());
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> appendToken(res, parameter);
                    }
                }
                case State.SINGLE_QUOTED, State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED -> appendToken(res, parameter);
                    }
                }
            }
        }
        return res;
    }

    private Result handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> ResultDefault.FAIL;
        };
    }
}
