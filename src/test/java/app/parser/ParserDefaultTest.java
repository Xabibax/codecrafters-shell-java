package app.parser;

import app.models.ast.AST;
import app.models.ast.CommandNode;
import app.models.token.Tokens;
import app.models.token.word.WordDefault;
import app.models.token.word.Words;
import app.models.token.word.wordpart.Literal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ParserDefaultTest {

    static Stream<Arguments> providedParameters() {
        return Stream.of(
                Arguments.of(test1()),
                Arguments.of(test2()),
                Arguments.of(test3())

        );
    }

    static ApplyTestParameters test1() {
        WordDefault invalidAppleCommand = new WordDefault(new Literal("invalid_apple_command"));
        final var input = Tokens.of(invalidAppleCommand);
        final var expected = new CommandNode(invalidAppleCommand, Words.of());

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test2() {
        final var input = Tokens.of("echo", "pineapple", "apple");
        final var expected = new CommandNode("echo", Words.of("pineapple", "apple"));

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test3() {
        final var input = Tokens.of("bash.exe", "-c", "echo 1 2 3");
        final var expected = new CommandNode("bash.exe", Words.of("-c", "echo 1 2 3"));

        return new ApplyTestParameters(input, expected);
    }

    @ParameterizedTest
    @MethodSource("providedParameters")
    void apply(ApplyTestParameters applyTestParameters) {
        final var parserDefault = new ParserDefault();

        final var actual = parserDefault.apply(applyTestParameters.input());

        Assertions.assertEquals(applyTestParameters.expected, actual);
    }

    record ApplyTestParameters(Tokens input, AST expected) {
    }
}