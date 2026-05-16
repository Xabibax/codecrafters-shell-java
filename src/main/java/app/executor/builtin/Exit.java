package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;

import java.util.function.Function;

public record Exit(AppContext appContext) implements Function<CommandNode, Integer> {

    @Override
    public Integer apply(CommandNode commandNode) {
        System.exit(AppContext.SUCCESS);
        return AppContext.SUCCESS;
    }
}
