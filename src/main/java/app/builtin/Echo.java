package app.builtin;

import app.Context;
import app.Parameters;
import app.builtin.utils.FormatUtil;

import java.util.function.Function;

public record Echo(Context context) implements Function<String, Integer> {


    @Override
    public Integer apply(String line) {
        final Parameters parameters;
        try {
            parameters = FormatUtil.format(line)
                    .subList(1);
        } catch (FormatUtil.OddNumberSingleQuotesException e) {
            return Context.FAIL;
        }

        IO.println(parameters.toString());
        return Context.SUCCESS;
    }

}
