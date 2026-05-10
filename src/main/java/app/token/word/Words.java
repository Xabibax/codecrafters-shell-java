package app.token.word;

import app.token.ITokens;
import app.token.Token;
import app.token.Tokens;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class Words extends ArrayList<Word> implements ITokens<Word, Words> {

    public Words() {
        super();
    }

    public static Words of(Word... words) {
        Words res = new Words();
        res.addAll(Arrays.asList(words));
        return res;
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

    public Words trim(){
        while (!isEmpty() && Token.State.SPACE.equals(getFirst().state())) {
            removeFirst();
        }
        while (!isEmpty() && Token.State.SPACE.equals(getLast().state())) {
            removeLast();
        }
        return this;
    }

    @Override
    public String toString() {
        return tokensJoiner();
    }

    public Words subList(int indexStart, int indexEnd) {
        return super.subList(indexStart, indexEnd).stream().collect(Words.toList());
    }

    public Words subList(int indexStart) {
        return super.subList(indexStart, this.size()).stream().collect(Words.toList());
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
