package app.models.token.word;

import app.models.token.word.wordpart.WordPart;
import app.models.token.word.wordpart.WordParts;
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
