package app.lexer;

public class IncoherentFinalStateException extends RuntimeException {

    public IncoherentFinalStateException(LexerState state) {
        super("Lexer ended with a wrong state: %s".formatted(state));
    }

}
