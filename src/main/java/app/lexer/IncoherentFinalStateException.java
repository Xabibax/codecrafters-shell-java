package app.lexer;

public class IncoherentFinalStateException extends RuntimeException {

    IncoherentFinalStateException(State state) {
        super("Lexer ended with a wrong state: %s".formatted(state));
    }

}
