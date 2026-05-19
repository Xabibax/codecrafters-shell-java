package app.lexer;

public class IncoherentFinalStateException extends RuntimeException {

    public IncoherentFinalStateException(State state) {
        super("Lexer ended with a wrong state: %s".formatted(state));
    }

}
