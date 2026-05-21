package app.executor.executable;

import app.models.ast.CommandNode;
import app.models.result.Result;

import java.util.function.Function;

public interface Executable extends Function<CommandNode, Result> {
}
