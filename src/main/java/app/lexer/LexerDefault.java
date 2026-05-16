package app.lexer;

import app.AppContext;
import app.lexer.token.Token;
import app.lexer.token.Tokens;

public record LexerDefault(AppContext appContext) implements Lexer {

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

}
