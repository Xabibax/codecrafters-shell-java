import app.Context;
import builtin.Command;

import static builtin.Command.EXECUTABLE;
import static builtin.Command.NOT_FOUND;

@SuppressWarnings("InfiniteLoopStatement")
void main() {
    Context context = new Context();
    do handleCommand(context); while (true);
}

private int handleCommand(Context context) {
    printPrompt();
    final var line = IO.readln();

    final var splitLine = Arrays.stream(line.split(" ")).toList();

    final var commandLabel = splitLine.getFirst();

    Command command = Command.getCommandFrom(commandLabel);
    if(NOT_FOUND.equals(command)) {
        command = context.handleExecutableSearch(commandLabel).isPresent() ? EXECUTABLE : NOT_FOUND;
    }

    return command.apply(context, line);
}
private void printPrompt() {
    IO.print("$ ");
}