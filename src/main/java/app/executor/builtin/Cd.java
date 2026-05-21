package app.executor.builtin;

import app.AppContext;
import app.models.ast.CommandNode;
import app.models.result.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import static app.models.result.ResultDefault.FAIL;
import static app.models.result.ResultDefault.SUCCESS;

public record Cd(AppContext appContext) implements Function<CommandNode, Result> {
    @Override
    public Result apply(CommandNode commandNode) {

        if (commandNode.parameters()
                .isEmpty()) {
            appContext.setCurrendDirectory(appContext.getHomeDirectory());
        }
        String path = commandNode.parameters()
                .getFirst()
                .value()
                ;
        if ("~".equalsIgnoreCase(path)) {
            appContext.setCurrendDirectory(appContext.getHomeDirectory());
        }

        var currentDirectory = appContext.getCurrentDirectory();
        if (path.startsWith("~")) {
            currentDirectory = appContext.getHomeDirectory();
            path = "." + path.substring(1);
        }

        final var newDirectory = currentDirectory.resolve(path)
                .toAbsolutePath();

        try {
            Path canonicalDirectory = handleChangeCurrentDirectory(currentDirectory, newDirectory);
            appContext.setCurrendDirectory(canonicalDirectory);
        } catch (IOException e) {
            return FAIL;
        }
        return SUCCESS;
    }

    private Path handleChangeCurrentDirectory(Path currentDirectory, Path newDirectory) throws IOException {
        if (!newDirectory.toAbsolutePath()
                .toFile()
                .isDirectory()) {
            IO.println("cd: %s: No such file or directory".formatted(newDirectory));
            return currentDirectory;
        }
        return newDirectory.toFile()
                .getCanonicalFile()
                .toPath();
    }
}
