package app.lexer.token;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Tokens extends ArrayList<Token> {

    public Tokens() {
        super();
    }

    public static Tokens of(Token... tokens) {
        Tokens res = new Tokens();
        res.addAll(Arrays.asList(tokens));
        return res;
    }

    public static Collector<Token, Tokens, Tokens> toList() {
        return new Tokens.CollectorImpl<>(Tokens::new,
                Tokens::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    public static Collector<Token, Tokens, String> joining() {
        return new CollectorImpl<>(
                Tokens::new,
                ArrayList::add,
                (r1, r2) -> {
                    r1.addAll(r2);
                    return r1;
                },
                Tokens::tokensJoiner,
                Collections.emptySet());
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    @Override
    public String toString() {
        return tokensJoiner();
    }

    public Tokens toTokens() {
        return this;
    }

    public String tokensJoiner() {
        final List<String> res = new ArrayList<>();
        int size = size();
        for (int i = 0; i < size; i++) {
            final var parameter = get(i);
            if (i == 0) {
                res.add(parameter.value());
                continue;
            }
            final var previousParameter = get(i - 1);
            switch (parameter.state()) {
                case State.NORMAL -> {
                    switch (previousParameter.state()) {
                        case NORMAL -> res.add(parameter.value());
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> res.set(res.size() - 1, res.getLast() + parameter.value());
                    }
                }
                case State.SINGLE_QUOTED, State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED ->
                                res.set(res.size() - 1, res.getLast() + parameter.value());
                    }
                }
            }
        }
        return String.join(" ", res);
    }

    public List<Token> subList(int i) {
        if (i > size()) {
            return new Tokens();
        }
        return subList(i, size());
    }

    private record CollectorImpl<T, R>(Supplier<Tokens> supplier,
                                       BiConsumer<Tokens, T> accumulator,
                                       BinaryOperator<Tokens> combiner,
                                       Function<Tokens, R> finisher,
                                       Set<Characteristics> characteristics
    ) implements Collector<T, Tokens, R> {

        CollectorImpl(Supplier<Tokens> supplier,
                      BiConsumer<Tokens, T> accumulator,
                      BinaryOperator<Tokens> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
    }

}
