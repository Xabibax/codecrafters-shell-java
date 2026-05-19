package app.ast;

import app.AppContext;
import app.executor.Result;
import app.executor.ResultDefault;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public record RedirectOutputNode(AST ast, Path target) implements AST {
    @Override
    public Result apply(AppContext appContext) {
        final var result = ast.apply(appContext);

        File file = target.toFile();

        try {
            file.createNewFile();
        } catch (IOException e) {
            return ResultDefault.ioFail(e.getMessage());
        }

        if (!file.setWritable(true)) {
            return ResultDefault.fail("Cannot write file: %s".formatted(file.toString()));
        }

        try {
            Files.writeString(target, result.getOutput(), Charset.defaultCharset());
        } catch (IOException e) {
            return ResultDefault.ioFail(e.getMessage());
        }

        return ResultDefault.SUCCESS;
    }
}
