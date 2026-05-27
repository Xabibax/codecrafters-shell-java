package app.executor.executable;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;

import java.util.function.BiFunction;

public interface Executable extends BiFunction<CommandNode, AppContext, Result> {
}
