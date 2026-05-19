package app.executor.executable;

import app.ast.CommandNode;
import app.executor.Result;

import java.util.function.Function;

public interface Executable extends Function<CommandNode, Result> {
}
