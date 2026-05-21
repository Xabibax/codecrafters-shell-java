package app.models.token;

import app.models.token.wordpart.WordPart;

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

    public Tokens(Collection<? extends Token> c) {
        super(c);
    }

    public static Tokens of(Token... tokens) {
        Tokens res = new Tokens();
        res.addAll(Arrays.asList(tokens));
        return res;
    }

    public static Tokens of(WordPart... wordParts) {
        final var words = Arrays.stream(wordParts)
                .map(WordDefault::of)
                .toList()
                ;
        return new Tokens(words);
    }

    public static Tokens of(String... literals) {
        final var words = Arrays.stream(literals)
                .map(WordDefault::of)
                .toList()
                ;
        return new Tokens(words);
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
                Tokens::finisher,
                Collections.emptySet());
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tokens tokens)) {
            return false;
        }
        if (size() != tokens.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            if (!get(i).equals(tokens.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.join(" ",
                stream().map(Token::toString)
                        .toArray(String[]::new));
    }

    private String finisher() {
        return toString();
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
