package app.lexer;

import app.models.token.Tokens;
import app.models.token.operator.Operator;
import app.models.token.word.Word;
import app.models.token.word.WordDefault;
import app.models.token.word.wordpart.DoubleQuoted;
import app.models.token.word.wordpart.Literal;
import app.models.token.word.wordpart.SingleQuoted;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class LexerDefaultTest {


    static Stream<Arguments> providedParameters() {
        return Stream.of(Arguments.of(test1()),
                Arguments.of(test2()),
                Arguments.of(test3()),
                Arguments.of(test4()),
                Arguments.of(test5()),
                Arguments.of(test6()),
                Arguments.of(test7()),
                Arguments.of(test8()),
                Arguments.of(test9()),
                Arguments.of(test10())
        );
    }

    static ApplyTestParameters test1() {
        final var input = "invalid_apple_command";
        final var expected = Tokens.of(new WordDefault(new Literal("invalid_apple_command")));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test2() {
        final var input = "echo pineapple apple";
        final var expected = Tokens.of("echo", "pineapple", "apple");

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test3() {
        final var input = "bash.exe -c \"echo 1 2 3\"";
        final var expected = Tokens.of(new Literal("bash.exe"), new Literal("-c"), new DoubleQuoted("echo 1 2 3"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test4() {
        final var input = "bash.exe -c \"echo\"' 1 '2\" 3\"";
        final var echo = new DoubleQuoted("echo");
        final var singleQuoted1 = new SingleQuoted(" 1 ");
        final var literal2 = new Literal("2");
        final var doubleQuoted3 = new DoubleQuoted(" 3");
        final var command = Word.of(echo, singleQuoted1, literal2, doubleQuoted3);
        final var expected = Tokens.of(Word.of("bash.exe"), Word.of("-c"), command);

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test5() {
        final var input = "echo \"test  example\"  \"shell\"\"world\"";
        final var expected = Tokens.of(Word.of("echo"),
                Word.of(new DoubleQuoted("test  example")),
                Word.of(new DoubleQuoted("shell"), new DoubleQuoted("world")));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test6() {
        final var input = "echo \"shell\"  \"world's\"  script\"\"example";
        final var expected = Tokens.of(Word.of("echo"),
                Word.of(new DoubleQuoted("shell")),
                Word.of(new DoubleQuoted("world's")),
                Word.of(new Literal("script"),new DoubleQuoted(""), new Literal("example")));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test7() {
        final var input = "ls -1 /tmp/rat > /tmp/dog/ant.md";
        final var expected = Tokens.of(Word.of("ls"),
                Word.of("-1"),
                Word.of("/tmp/rat"),
                Operator.of(">"),
                Word.of("/tmp/dog/ant.md"));

        return new ApplyTestParameters(input, expected);
    }
    static ApplyTestParameters test8() {
        final var input = "cat /tmp/fox/banana nonexistent 1> /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of("1>"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test9() {
        final var input = "cat /tmp/fox/banana nonexistent > /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of(">"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test10() {
        final var input = "cat /tmp/fox/banana nonexistent 2> /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of("2>"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test11() {
        final var input = "cat /tmp/fox/banana nonexistent 2>> /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of("2>>"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test12() {
        final var input = "cat /tmp/fox/banana nonexistent 1>> /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of("1>>"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test13() {
        final var input = "cat /tmp/fox/banana nonexistent >> /tmp/pig/rat.md";
        final var expected = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of(">>"),
                Word.of("/tmp/pig/rat.md"));

        return new ApplyTestParameters(input, expected);
    }

    @ParameterizedTest
    @MethodSource("providedParameters")
    void apply(ApplyTestParameters applyTestParameters) {
        final var lexerDefault = new LexerDefault();

        final var actual = lexerDefault.apply(applyTestParameters.input());

        Assertions.assertEquals(applyTestParameters.expected, actual);
    }

    record ApplyTestParameters(String input, Tokens expected) {
    }
}