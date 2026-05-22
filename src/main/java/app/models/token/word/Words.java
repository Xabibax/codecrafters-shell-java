package app.models.token.word;

import app.models.token.Tokens;
import app.models.token.TokensUtils;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class Words extends ArrayList<Word> {

    public Words() {
        super();
    }

    public Words(Collection<? extends Word> c) {
        super(c);
    }

    public static Words of(Word... words) {
        Words res = new Words();
        res.addAll(Arrays.asList(words));
        return res;
    }

    public static Words of() {
        return new Words();
    }

    public static Words of(String... literals) {
        final var list = Arrays.stream(literals)
                .map(Word::of)
                .toList()
                ;
        return new Words(list);
    }

    public static Collector<Word, ?, Words> toList() {
        return new CollectorImpl<>(Words::new,
                Words::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    @Override
    public String toString() {
        return TokensUtils.tokensJoiner(this.toTokens());
    }

    @Override
    @NonNull
    public Words subList(int indexStart, int indexEnd) {
        return super.subList(indexStart, indexEnd)
                .stream()
                .collect(Words.toList());
    }

    public Words subList(int indexStart) {
        return super.subList(indexStart, this.size())
                .stream()
                .collect(Words.toList());
    }

    public Tokens toTokens() {
        return stream().collect(Tokens.toList());
    }

    private record CollectorImpl<R>(Supplier<Words> supplier,
                                    BiConsumer<Words, Word> accumulator,
                                    BinaryOperator<Words> combiner,
                                    Function<Words, R> finisher,
                                    Set<Characteristics> characteristics
    ) implements Collector<Word, Words, R> {

        @SuppressWarnings("unchecked")
        CollectorImpl(Supplier<Words> supplier,
                      BiConsumer<Words, Word> accumulator,
                      BinaryOperator<Words> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, i -> (R) i, characteristics);
        }
    }


}
