package app.models.result;

import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;

public record ResultDefault(String output, String errorOutput, int code) implements Result {

    public ResultDefault(String output, int code){
        this(output, null, code);
    }

    public ResultDefault(OutputStream output, int code){
        this(output.toString(), null, code);
    }

    public ResultDefault(OutputStream output,OutputStream errorOutput, int code){
        this(output.toString(), errorOutput.toString(), code);
    }

    final public static Result SUCCESS = ResultDefault.success("");
    final public static Result WARNING = ResultDefault.warning("");

    public static Result success(String output) {
        return new ResultDefault(output, Result.SUCCESS);
    }

    public static Result warning(String output) {
        return new ResultDefault(output, Result.WARNING);
    }

    public static Result fail(String output) {
        return new ResultDefault(null, output, Result.FAIL);
    }

    public static Result ioFail(String output) {
        return new ResultDefault(null, output, Result.IO_FAIL);
    }

    public static Result handleExecutableException(Exception e) {
        return switch (e) {
            case IOException _ -> ioFail(e.getMessage());
            default -> fail(e.getMessage());
        };
    }

    @Override
    public String getOutput() {
        return output();
    }

    @Override
    public String getErrorOutput() {
        return errorOutput();
    }

    @Override
    public int getCode() {
        return code();
    }

}
