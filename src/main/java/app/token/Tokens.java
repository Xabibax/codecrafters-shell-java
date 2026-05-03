package app.token;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Tokens extends ArrayList<Token> {

    public Tokens() {
        super();
    }

    public static Tokens of(Token... tokens) {
        Tokens res = new Tokens();
        res.addAll(Arrays.asList(tokens));
        return res;
    }

    public static Collector<Token, ?, Tokens> toList() {
        return new Tokens.CollectorImpl<>(Tokens::new,
                Tokens::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    @Override
    public String toString() {
        return stream().map(Token::value).collect(Collectors.joining(" "));
    }

    public record CollectorImpl<R>(Supplier<Tokens> supplier,
                                   BiConsumer<Tokens, Token> accumulator,
                                   BinaryOperator<Tokens> combiner,
                                   Function<Tokens, R> finisher,
                                   Set<Characteristics> characteristics
    ) implements Collector<Token, Tokens, R> {

        @SuppressWarnings("unchecked")
        CollectorImpl(Supplier<Tokens> supplier,
                      BiConsumer<Tokens, Token> accumulator,
                      BinaryOperator<Tokens> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, i -> (R) i, characteristics);
        }
    }

}
