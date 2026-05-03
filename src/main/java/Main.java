import app.Context;
import app.builtin.Command;

import static app.builtin.Command.EXECUTABLE;
import static app.builtin.Command.NOT_FOUND;

@SuppressWarnings("InfiniteLoopStatement")
void main() {
    Context context = new Context();
    do handleCommand(context); while (true);
}

private void handleCommand(Context context) {
    printPrompt();
    final var line = IO.readln();

    final var splitLine = Arrays.stream(line.split(" ")).toList();

    final var commandLabel = splitLine.getFirst();

    Command command = Command.getCommandFrom(commandLabel);
    if(NOT_FOUND.equals(command)) {
        command = context.handleExecutableSearch(commandLabel).isPresent() ? EXECUTABLE : NOT_FOUND;
    }

    command.apply(context, line);
}
private void printPrompt() {
    IO.print("$ ");
}