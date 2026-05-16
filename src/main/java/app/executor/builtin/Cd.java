package app.executor.builtin;

import app.AppContext;
import app.ast.CommandNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

public record Cd(AppContext appContext) implements Function<CommandNode, Integer> {
    @Override
    public Integer apply(CommandNode commandNode) {

        if (commandNode.parameters().isEmpty()) {
            appContext.setCurrendDirectory(appContext.getHomeDirectory());
        }
        String path = commandNode.parameters().getFirst().value();
        if ("~".equalsIgnoreCase(path)) {
            appContext.setCurrendDirectory(appContext.getHomeDirectory());
        }

        var currentDirectory = appContext.getCurrentDirectory();
        if (path.startsWith("~")) {
            currentDirectory = appContext.getHomeDirectory();
            path = "." + path.substring(1);
        }

        final var newDirectory = currentDirectory.resolve(path).toAbsolutePath();

        try {
            Path canonicalDirectory = handleChangeCurrentDirectory(currentDirectory, newDirectory);
            appContext.setCurrendDirectory(canonicalDirectory);
        } catch (IOException e) {
            return AppContext.FAIL;
        }
        return AppContext.SUCCESS;
    }

    private Path handleChangeCurrentDirectory(Path currentDirectory, Path newDirectory) throws IOException {
        if (!newDirectory.toAbsolutePath().toFile().isDirectory()) {
            IO.println("cd: %s: No such file or directory".formatted(newDirectory));
            return currentDirectory;
        }
        return newDirectory.toFile().getCanonicalFile().toPath();
    }
}
