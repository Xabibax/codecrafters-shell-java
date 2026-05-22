package app.models.token.word.wordpart;

import org.jspecify.annotations.NonNull;

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

    public WordParts(Collection<? extends WordPart> c) {
        super(c);
    }

    @NonNull
    public static WordParts of(WordPart... wordParts) {
        WordParts res = new WordParts();
        res.addAll(Arrays.asList(wordParts));
        return res;
    }

    @NonNull
    public static WordParts of(String... wordParts) {
        List<WordPart> list = Arrays.stream(wordParts)
                .map(Literal::new)
                .map(WordPart.class::cast)
                .toList()
                ;
        return new WordParts(list);
    }

    @NonNull
    public static Collector<WordPart, ?, WordParts> toList() {
        Supplier<WordParts> supplier = WordParts::new;
        BiConsumer<WordParts, WordPart> accumulator = WordParts::add;
        BinaryOperator<WordParts> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        Set<Collector.Characteristics> characteristics = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
        return new CollectorImpl<>(supplier, accumulator, combiner, characteristics);
    }

    @Override
    public String toString() {
        return stream().map(WordPart::value)
                .collect(Collectors.joining(""));
    }

    @NonNull
    public WordParts subList(int indexStart, int indexEnd) {
        return super.subList(indexStart, indexEnd)
                .stream()
                .collect(WordParts.toList());
    }

    @NonNull
    public WordParts subList(int indexStart) {
        return super.subList(indexStart, this.size())
                .stream()
                .collect(WordParts.toList());
    }

    public String values() {
        return stream().map(WordPart::value)
                .collect(Collectors.joining());
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
