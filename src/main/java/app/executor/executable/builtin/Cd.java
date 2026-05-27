package app.executor.executable.builtin;

import app.AppContext;
import app.executor.executable.Executable;
import app.models.ast.CommandNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.io.IOException;
import java.nio.file.Path;

import static app.models.result.ResultDefault.SUCCESS;

public record Cd() implements Executable {
    @Override
    public Result apply(CommandNode commandNode, AppContext appContext) {

        if (commandNode.parameters()
                .isEmpty()) {
            appContext.setCurrentDirectory(appContext.getHomeDirectory());
        }
        String path = commandNode.parameters()
                .getFirst()
                .value()
                ;
        if ("~".equalsIgnoreCase(path)) {
            appContext.setCurrentDirectory(appContext.getHomeDirectory());
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
            appContext.setCurrentDirectory(canonicalDirectory);
        } catch (IOException e) {
            return ResultDefault.handleExecutableException(e);
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
