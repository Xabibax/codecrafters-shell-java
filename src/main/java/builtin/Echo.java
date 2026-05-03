package builtin;

import app.Context;
import app.Parameters;
import builtin.utils.FormatUtil;

import java.util.ArrayList;
import java.util.function.Function;

public record Echo(Context context) implements Function<String, Integer> {


    @Override
    public Integer apply(String line) {
        final Parameters parameters;
        try {
            parameters = FormatUtil.format(line).stream().skip(1).collect(Parameters::new, ArrayList::add, ArrayList::addAll);
        } catch (FormatUtil.OddNumberSingleQuotesException e) {
            return Context.FAIL;
        }

        IO.println(parameters.toString());
        return Context.SUCCESS;
    }

}
