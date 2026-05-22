package app.models.result;

import lombok.Getter;

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
    final public static Result FAIL = ResultDefault.fail("");
    final public static Result IO_FAIL = ResultDefault.ioFail("");

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
