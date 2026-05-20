package app.lexer;

import app.AppContext;
import app.lexer.token.Tokens;
import app.lexer.token.WordDefault;
import app.lexer.token.wordpart.Literal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class LexerDefaultTest {


    static Stream<Arguments> providedInput() {
        return Stream.of(
                Arguments.of(test1()),
                Arguments.of(test2())

        );
    }

    static ApplyTestParameters test1() {
        String input = "invalid_apple_command";
        Tokens tokens = Tokens.of(new WordDefault(new Literal("invalid_apple_command")));

        return new ApplyTestParameters(input, tokens);
    }

    static ApplyTestParameters test2() {
        String input = "invalid_command_1";
        Tokens tokens = Tokens.of(new WordDefault(new Literal("invalid_command_1")));

        return new ApplyTestParameters(input, tokens);
    }

    @ParameterizedTest
    @MethodSource("providedInput")
    void apply(ApplyTestParameters applyTestParameters) {
        LexerDefault lexerDefault = new LexerDefault(new AppContext());

        Tokens actual = lexerDefault.apply(applyTestParameters.input());

        Assertions.assertEquals(applyTestParameters.expected, actual);
    }

    record ApplyTestParameters(String input, Tokens expected) {
    }
}