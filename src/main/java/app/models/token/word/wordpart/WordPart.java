package app.models.token.word.wordpart;

public sealed interface WordPart permits DoubleQuoted, Escaped, Literal, SingleQuoted {

    String value();
}
