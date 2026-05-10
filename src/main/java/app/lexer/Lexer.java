package app.lexer;

import app.Context;
import app.token.Token;
import app.token.TokenBuilder;
import app.token.Tokens;

import java.util.function.Function;

import static app.lexer.Lexer.State.NORMAL;

public record Lexer(Context appContext) implements Function<String, Tokens> {

    @Override
    public Tokens apply(String input) throws IncoherentFinalStateException {
        final var context = new LexerContext(input);
        final var handleCharacter = new HandleCharacter(context);
        if (context.isAtEnd()) {
            return context.tokens;
        }

        while (!context.isAtEnd()) {
            handleCharacter.handleChar(context.nextChar());
        }

        if (context.tokenBuilder.isNonEmpty()) {
            final var lastToken = context.tokenBuilder.build();
            context.tokens.add(lastToken);
        }

        handleMergeableTokens(context);
        handleTrim(context);
        if (!context.state.isTerminal()) {
            throw new IncoherentFinalStateException(context.state);
        }

        return context.tokens;
    }

    private void handleTrim(LexerContext context) {
        context.tokens.trim();
    }

    boolean tokenToKeep(Token token) {
        return switch (token.state()) {
            case SINGLE_QUOTED, DOUBLE_QUOTED -> !token.value().isEmpty();
            case SPACE -> false;
            case NORMAL -> true;
        };
    }

    Token merge(Token currToken, Token nextToken) {
        String value = currToken.value() + nextToken.value();
        Token.State state = currToken.state();
        return Token.builder().state(state).value(value).build();
    }

    void handleMergeableTokens(LexerContext context) {
        final var tokens = context.tokens();

        for (int i = 0; i < tokens.size() - 1; i++) {
            final var currToken = tokens.get(i);
            final var nextToken = tokens.get(i + 1);

            boolean sameMergeableToken = currToken.state().isMergeable() && currToken.state().equals(nextToken.state());
            boolean emptyToken = currToken.value().isEmpty() || nextToken.value().isEmpty();
            if (sameMergeableToken || emptyToken) {
                Token token = merge(currToken, nextToken);
                tokens.set(i, token);
                tokens.remove(i + 1);
                i--;
            }
        }
    }

    public enum State {
        NORMAL, SINGLE_QUOTES_OPEN(false), DOUBLE_QUOTES_OPEN(false), SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_CLOSE, SPACE,
        ;

        final boolean isTerminal;

        State() {
            this.isTerminal = true;
        }

        State(boolean isTerminal) {
            this.isTerminal = isTerminal;
        }

        boolean isTerminal() {
            return this.isTerminal;
        }
    }

    static class LexerContext {
        String input;
        int pos;
        Tokens tokens = new Tokens();
        TokenBuilder tokenBuilder = Token.builder();
        State state = NORMAL;
        boolean escape = false;

        LexerContext(String input) {
            this.input = input;
            this.pos = 0;
        }

        public void handleTokenEnd() {
            Token token = tokenBuilder.build();
            tokens.add(token);
            tokenBuilder.reset();
            switch (state) {
                case NORMAL, SPACE -> state = State.NORMAL;
                case SINGLE_QUOTES_OPEN, SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_OPEN, DOUBLE_QUOTES_CLOSE -> {
                }
            }
        }

        public Tokens getTokens() {
            return tokens;
        }

        public void setTokens(Tokens tokens) {
            this.tokens = tokens;
        }

        public TokenBuilder getTokenBuilder() {
            return tokenBuilder;
        }

        public void setTokenBuilder(TokenBuilder tokenBuilder) {
            this.tokenBuilder = tokenBuilder;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        public Tokens tokens() {
            return this.tokens;
        }

        public void setEscape(boolean escape) {
            this.escape = escape;
        }

        public boolean isEscape() {
            return escape;
        }

        public boolean isAtEnd() {
            return input.length() == pos;
        }

        /**
         * return the currentChar and increment position
         */
        public char nextChar() {
            return input.charAt(pos++);
        }
    }

    public static class IncoherentFinalStateException extends RuntimeException {

        public IncoherentFinalStateException(State state) {
            super("Lexer ended with a wrong state: %s".formatted(state));
        }

    }
}
