package app.lexer.token.wordpart;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class WordParts extends ArrayList<WordPart> {

    public WordParts() {
        super();
    }

    public static WordParts of(WordPart... wordParts) {
        WordParts res = new WordParts();
        res.addAll(Arrays.asList(wordParts));
        return res;
    }

    public static Collector<WordPart, ?, WordParts> toList() {
        return new CollectorImpl<>(WordParts::new, WordParts::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    @Override
    public String toString() {
        return stream().map(WordPart::value).collect(Collectors.joining(""));
    }

    public WordParts subList(int indexStart, int indexEnd) {
        return super.subList(indexStart, indexEnd).stream().collect(WordParts.toList());
    }

    public WordParts subList(int indexStart) {
        return super.subList(indexStart, this.size()).stream().collect(WordParts.toList());
    }

    private record CollectorImpl<R>(Supplier<WordParts> supplier, BiConsumer<WordParts, WordPart> accumulator,
                                    BinaryOperator<WordParts> combiner, Function<WordParts, R> finisher,
                                    Set<Characteristics> characteristics) implements Collector<WordPart, WordParts, R> {

        @SuppressWarnings("unchecked")
        CollectorImpl(Supplier<WordParts> supplier, BiConsumer<WordParts, WordPart> accumulator, BinaryOperator<WordParts> combiner, Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, i -> (R) i, characteristics);
        }
    }


}
