package app.executor;

import app.AppContext;
import app.models.ast.AST;
import app.models.ast.CommandNode;
import app.models.ast.Redirect;
import app.models.ast.RedirectNode;
import app.models.result.Result;
import app.models.result.ResultDefault;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;

public record ExecutorDefault() implements Executor {
    private static OutputStream getOutputStream(AppContext appContext, Redirect redirect) throws GetOutputStreamIOException {
        return switch (redirect.target()) {
            case Redirect.RedirectToFile redirectToFile -> {
                try {
                    boolean newFile = redirectToFile.target()
                            .toFile()
                            .createNewFile()
                            ;
                    yield Files.newOutputStream(redirectToFile.target(), redirect.getOpenOptions());
                } catch (IOException e) {
                    throw new GetOutputStreamIOException(e);
                }
            }
            case Redirect.RedirectToErr _ -> appContext.getStderr();
            case Redirect.RedirectToOut _ -> appContext.getStdout();
        };
    }

    @Override
    public Result apply(AST ast, AppContext appContext) {
        return switch (ast) {
            case CommandNode commandNode -> executeCommand(commandNode, appContext);
            case RedirectNode redirectNode -> executeRedirect(redirectNode, appContext);
        };
    }

    private Result executeRedirect(RedirectNode redirectNode, AppContext appContext) {
        try {
            redirectNode.redirects()
                    .forEach(redirect -> {
                        final OutputStream output = getOutputStream(appContext, redirect);
                        final PrintStream printStream = new PrintStream(output);
                        switch (redirect.source()) {
                            case ERR -> appContext.setStderr(printStream);
                            case OUT -> appContext.setStdout(printStream);
                        }

                    });
            return redirectNode.apply(appContext);
        } catch (GetOutputStreamIOException e) {
            return ResultDefault.handleExecutableException(e.getCause());
        }

    }

    private Result executeCommand(CommandNode commandNode, AppContext appContext) {
        return commandNode.apply(appContext);
    }

    private static class GetOutputStreamIOException extends RuntimeException {
        public GetOutputStreamIOException(IOException e) {
            super(e);
        }

        public IOException getCause() {
            return (IOException) super.getCause();
        }
    }
}
