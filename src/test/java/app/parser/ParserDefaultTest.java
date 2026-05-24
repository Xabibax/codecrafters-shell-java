package app.parser;

import app.models.ast.AST;
import app.models.ast.CommandNode;
import app.models.ast.Redirect;
import app.models.ast.RedirectNode;
import app.models.token.Tokens;
import app.models.token.operator.Operator;
import app.models.token.word.Word;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

class ParserDefaultTest {

    static Stream<Arguments> providedParameters() {
        return Stream.of(
                Arguments.of(test1()),
                Arguments.of(test2()),
                Arguments.of(test3()),
                Arguments.of(test4())

        );
    }

    static ApplyTestParameters test1() {
        final var input =  Tokens.of("invalid_apple_command");
        final var expected = new CommandNode("invalid_apple_command");

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test2() {
        final var input = Tokens.of("echo", "pineapple", "apple");
        final var expected = new CommandNode("echo", "pineapple", "apple");

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test3() {
        final var input = Tokens.of("bash.exe", "-c", "echo 1 2 3");
        final var expected = new CommandNode("bash.exe","-c", "echo 1 2 3");

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test4() {
        final var input = Tokens.of(Word.of("cat"),
                Word.of("/tmp/fox/banana"),
                Word.of("nonexistent"),
                Operator.of("1>"),
                Word.of("/tmp/pig/rat.md"));
        CommandNode commandNode = new CommandNode("cat","/tmp/fox/banana", "nonexistent");
        Redirect.RedirectToFile redirectToFile = new Redirect.RedirectToFile(Path.of("/tmp/pig/rat.md"));
        Redirect redirect = new Redirect(Redirect.RedirectSource.OUT, Redirect.RedirectType.WRITE, redirectToFile);
        final var expected = new RedirectNode(commandNode, List.of(redirect));

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