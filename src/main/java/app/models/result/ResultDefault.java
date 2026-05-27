package app.models.result;

import java.io.IOException;

public record ResultDefault(String message, int code) implements Result {

    final public static Result SUCCESS = ResultDefault.success();

    private static Result success() {
        return new ResultDefault(null, Result.SUCCESS);
    }

    public static Result warning(String output) {
        return new ResultDefault(output, Result.WARNING);
    }

    public static Result fail(String output) {
        return new ResultDefault(output, Result.FAIL);
    }

    private static Result ioFail(String message) {
        return new ResultDefault(message, Result.IO_FAIL);
    }

    public static Result handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> ioFail(e.getMessage());
            default -> fail(e.getMessage());
        };
    }
}
