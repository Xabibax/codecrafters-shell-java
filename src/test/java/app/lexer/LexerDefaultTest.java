package app.lexer;

import app.models.token.Tokens;
import app.models.token.WordDefault;
import app.models.token.wordpart.DoubleQuoted;
import app.models.token.wordpart.Literal;
import app.models.token.wordpart.SingleQuoted;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class LexerDefaultTest {


    static Stream<Arguments> providedParameters() {
        return Stream.of(Arguments.of(test1()), Arguments.of(test2()), Arguments.of(test3()), Arguments.of(test4()));
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
        final var command = WordDefault.of(echo, singleQuoted1, literal2, doubleQuoted3);
        final var expected = Tokens.of(WordDefault.of("bash.exe"), WordDefault.of("-c"), command);

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