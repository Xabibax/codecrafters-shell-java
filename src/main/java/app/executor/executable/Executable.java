package app.executor.executable;

import app.AppContext;
import app.ast.CommandNode;
import app.lexer.token.Token;
import app.lexer.token.word.Word;
import app.lexer.token.word.Words;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static app.AppContext.FAIL;
import static app.AppContext.IO_FAIL;

public record Executable(AppContext appContext) implements Function<CommandNode, Integer> {

    @Override
    public Integer apply(CommandNode commandNode) {

        final var commands = merger(commandNode.parameters());

        commands.addFirst(commandNode.command().value());

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
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> appendToken(res, parameter);
                    }
                }
                case Token.State.SINGLE_QUOTED, Token.State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED ->
                                appendToken(res, parameter);
                        case SPACE -> res.add(parameter.value());
                    }
                }
                case Token.State.SPACE -> {
                }
            }
        }
        return res;
    }

    private static void appendToken(List<String> res, Word parameter) {
        final var origin = res.getLast();
        final var toAppend = parameter.value();
        res.set(res.size() - 1, origin + toAppend);
    }

    private int handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> FAIL;
        };
    }
}
