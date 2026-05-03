package executable;

import app.Context;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static app.Context.IO_FAIL;
import static app.Context.FAIL;

public record Executable(Context context) implements Function<String, Integer> {

    @Override
    public Integer apply(String line) {

        final var splitLine = Arrays.stream(line.split(" ")).toList();

        final var pb = new ProcessBuilder(splitLine);
        pb.redirectErrorStream(true);

        try {
            final var process = pb.start();
            final var exitValue = process.waitFor();
            process.getInputStream().transferTo(System.out);

            return exitValue;

        } catch (Exception e) {
            return handleExecutableException(e);
        }
    }

    private int handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> IO_FAIL;
            default -> FAIL;
        };
    }
}
