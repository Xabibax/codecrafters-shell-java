package app.lexer.token;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Tokens extends ArrayList<Token> implements ITokens<Token, Tokens> {

    public Tokens() {
        super();
    }

    public static Tokens of(Token... tokens) {
        Tokens res = new Tokens();
        res.addAll(Arrays.asList(tokens));
        return res;
    }

    @Override
    public String toString() {
        return tokensJoiner();
    }

    public Tokens trim(){
        while (!isEmpty() && Token.State.SPACE.equals(getFirst().state())) {
            removeFirst();
        }
        while (!isEmpty() && Token.State.SPACE.equals(getLast().state())) {
            removeLast();
        }
        return this;
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
