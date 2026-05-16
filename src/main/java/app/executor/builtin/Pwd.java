package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;

import java.util.function.Function;

public record Pwd(AppContext appContext) implements Function<CommandNode, Integer> {

    @Override
    public Integer apply(CommandNode commandNode) {
        IO.println(appContext.getCurrentDirectory());

        return AppContext.SUCCESS;
    }
}
