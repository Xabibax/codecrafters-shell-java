package app.builtin;

import app.Context;
import app.ast.SimpleCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

public record Cd(Context context) implements Function<SimpleCommand, Integer> {
    @Override
    public Integer apply(SimpleCommand command) {

        if (command.parameters().isEmpty()) {
            context.setCurrendDirectory(context.getHomeDirectory());
        }
        String path = command.parameters().getFirst().value();
        if ("~".equalsIgnoreCase(path)) {
            context.setCurrendDirectory(context.getHomeDirectory());
        }

        var currentDirectory = context.getCurrentDirectory();
        if (path.startsWith("~")) {
            currentDirectory = context.getHomeDirectory();
            path = "." + path.substring(1);
        }

        final var newDirectory = currentDirectory.resolve(path).toAbsolutePath();

        try {
            Path canonicalDirectory = handleChangeCurrentDirectory(currentDirectory, newDirectory);
            context.setCurrendDirectory(canonicalDirectory);
        } catch (IOException e) {
            return Context.FAIL;
        }
        return Context.SUCCESS;
    }

    private Path handleChangeCurrentDirectory(Path currentDirectory, Path newDirectory) throws IOException {
        if (!newDirectory.toAbsolutePath().toFile().isDirectory()) {
            IO.println("cd: %s: No such file or directory".formatted(newDirectory));
            return currentDirectory;
        }
        return newDirectory.toFile().getCanonicalFile().toPath();
    }
}
