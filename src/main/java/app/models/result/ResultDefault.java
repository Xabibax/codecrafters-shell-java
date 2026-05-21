package app.models.result;

public record ResultDefault(String output, int code) implements Result {

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
        return new ResultDefault(output, Result.FAIL);
    }

    public static Result ioFail(String output) {
        return new ResultDefault(output, Result.IO_FAIL);
    }

    @Override
    public String getOutput() {
        return output();
    }

}
