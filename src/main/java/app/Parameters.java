package app;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static app.Parameters.Parameter;


public class Parameters extends ArrayList<Parameter> {

    public Parameters() {
        super();
    }

    @Override
    public String toString() {
        return stream().map(Parameter::value).collect(Collectors.joining(" "));
    }

    public record Parameter(String value) {

        public static ParameterBuilder builder() {
            return new ParameterBuilder();
        }

        public boolean isBlank() {
            return value.isBlank();
        }
        public boolean isPresent() {
            return !isBlank();
        }
    }

    public record ParameterBuilder(StringBuilder value) {

        ParameterBuilder() {
            this(new StringBuilder());
        }

        public Parameter build() {
            return new Parameter(value.toString());
        }
    }
}
