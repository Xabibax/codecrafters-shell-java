package app.models.token;

import app.models.token.wordpart.WordPart;
import app.models.token.wordpart.WordParts;
import org.jspecify.annotations.NonNull;

public record WordDefault(WordParts wordParts) implements Word {

    public WordDefault {
        WordParts copy = new WordParts();
        copy.addAll(wordParts);
        wordParts = copy;
    }

    public WordDefault(WordPart wordPart) {
        this(WordParts.of(wordPart));
    }

    public static Word of(WordPart... wordParts) {
        return new WordDefault(WordParts.of(wordParts));
    }

    public static Word of(String... literals) {
        return new WordDefault(WordParts.of(literals));
    }

    @Override
    public String value() {
        return wordParts().values();
    }

    @Override
    @NonNull
    public String toString() {
        return wordParts().toString();
    }
}
