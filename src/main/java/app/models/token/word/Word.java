package app.models.token.word;

import app.models.token.Token;
import app.models.token.word.wordpart.WordPart;
import app.models.token.word.wordpart.WordParts;

public sealed interface Word extends Token permits WordDefault {
    static Word of(WordPart... wordParts) {
        return new WordDefault(WordParts.of(wordParts));
    }

    static Word of(String... literals) {
        return new WordDefault(WordParts.of(literals));
    }
}
