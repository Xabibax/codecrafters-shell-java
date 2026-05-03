package app;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
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

    public Parameters subList(int indexStart, int indexEnd) {
        return super.subList(indexStart, indexEnd).stream().collect(Parameters.toList());
    }

    public Parameters subList(int indexStart) {
        return super.subList(indexStart, this.size()).stream().collect(Parameters.toList());
    }

    public static Collector<Parameter, ?, Parameters>  toList() {
        return new CollectorImpl<>(Parameters::new,
                Parameters::add,
                (left, right) -> { left.addAll(right); return left; },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    private record CollectorImpl<R>(Supplier<Parameters> supplier,
                                  BiConsumer<Parameters, Parameter> accumulator,
                                  BinaryOperator<Parameters> combiner,
                                  Function<Parameters, R> finisher,
                                  Set<Characteristics> characteristics
    ) implements Collector<Parameter, Parameters, R> {

        @SuppressWarnings("unchecked")
        CollectorImpl(Supplier<Parameters> supplier,
                      BiConsumer<Parameters, Parameter> accumulator,
                      BinaryOperator<Parameters> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, i -> (R) i, characteristics);
        }
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

    public static class ParameterBuilder {

        private final StringBuilder value;
        private boolean shouldTrim;

        ParameterBuilder() {
            this.value = new StringBuilder();
            this.shouldTrim = false;
        }

        public ParameterBuilder append(String value) {
            this.value.append(value);
            return this;
        }

        public ParameterBuilder shouldTrim(boolean shouldTrim) {
            this.shouldTrim = shouldTrim;
            return this;
        }

        public Parameter build() {
            return new Parameter(value.toString());
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
