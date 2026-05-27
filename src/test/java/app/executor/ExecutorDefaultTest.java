package app.executor;

import app.AppContext;
import app.Factory;
import app.executor.executable.ExecutableDefault;
import app.models.ast.AST;
import app.models.ast.CommandNode;
import app.models.ast.Redirect;
import app.models.ast.RedirectNode;
import app.models.result.Result;
import app.models.result.ResultDefault;
import app.models.token.word.WordDefault;
import app.models.token.word.Words;
import app.models.token.word.wordpart.Literal;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ExecutorDefaultTest {


    ExecutableDefault executable;

    Factory factory;

    AppContext appContext;

    static Stream<Arguments> providedParameters() {
        return Stream.of(
                Arguments.of(test1()),
                Arguments.of(test2())

        );
    }

    static ApplyTestParameters test1() {
        String value = "invalid_apple_command";
        WordDefault invalidAppleCommand = new WordDefault(new Literal(value));
        final var input = new CommandNode(invalidAppleCommand, Words.of(value));
        final var expected = ResultDefault.fail("invalid_apple_command: command not found");

        return new ApplyTestParameters(input, expected);
    }

    static ApplyTestParameters test2() {
        final var input = new CommandNode("echo", Words.of("pineapple", "apple"));
        final var expected = ResultDefault.SUCCESS;

        return new ApplyTestParameters(input, expected);
    }

    static Stream<Arguments> providedExecutableParameters() {
        return Stream.of(
                Arguments.of(testExecutable1())
        );
    }

    static ApplyTestParameters testExecutable1() {
        final var input = new CommandNode("bash.exe", Words.of("-c", "echo 1 2 3"));
        final var expected = ResultDefault.SUCCESS;

        return new ApplyTestParameters(input, expected);
    }

    @BeforeEach
    void setUp() {
        executable = mock(ExecutableDefault.class);
        factory = spy(new Factory());
        appContext = spy(new AppContext(this.factory));
    }

    @ParameterizedTest
    @MethodSource("providedParameters")
    void apply(ApplyTestParameters applyTestParameters) {
        final var executorDefault = new ExecutorDefault();

        final var actual = executorDefault.apply(applyTestParameters.input(),appContext);

        Assertions.assertEquals(applyTestParameters.expected, actual);
    }

    @ParameterizedTest
    @MethodSource("providedExecutableParameters")
    void applyExecutable(ApplyTestParameters applyTestParameters) throws IOException {
        final var executorDefault = new ExecutorDefault();

        doReturn(executable)
                .when(factory)
                .executable()
        ;
        doReturn(factory)
                .when(appContext)
                .getFactory()
        ;
        doReturn(Optional.of(File.createTempFile("test", ".tmp")))
                .when(appContext)
                .handleExecutableSearch(((CommandNode) applyTestParameters.input).command())
        ;
        doReturn(applyTestParameters.expected)
                .when(executable)
                .apply((CommandNode) applyTestParameters.input, appContext)
        ;

        final var actual = executorDefault.apply(applyTestParameters.input(),appContext);

        Assertions.assertEquals(applyTestParameters.expected, actual);
    }

    record ApplyTestParameters(AST input, Result expected) {
    }

}