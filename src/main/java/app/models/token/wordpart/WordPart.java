package app.models.token.wordpart;

public sealed interface WordPart permits DoubleQuoted, Escaped, Literal, SingleQuoted {

    String value();
}
