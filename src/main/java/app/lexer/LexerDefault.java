package app.lexer;

import app.AppContext;
import app.lexer.token.State;
import app.lexer.token.Token;
import app.lexer.token.Tokens;
import app.lexer.token.Word;

public record LexerDefault(AppContext appContext) implements Lexer {

    @Override
    public Tokens apply(String input) throws IncoherentFinalStateException {
        final var context = new Context(input);
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

    private void handleTrim(Context context) {
        context.tokens.trim();
    }

    boolean tokenToKeep(Token token) {
        return switch (token.state()) {
            case SINGLE_QUOTED, DOUBLE_QUOTED -> !token.value().isEmpty();
            case SPACE -> false;
            case NORMAL -> true;
        };
    }

    Token merge(Word currToken, Word nextToken) {
        String value = currToken.value() + nextToken.value();
        State state = currToken.state();
        return Token.builder().state(state).value(value).build();
    }

    void handleMergeableTokens(Context context) {
        final var tokens = context.tokens();

        for (int i = 0; i < tokens.size() - 1; i++) {
            final var currToken = tokens.get(i);
            final var nextToken = tokens.get(i + 1);

            boolean sameMergeableToken = currToken.state().isMergeable() && currToken.state().equals(nextToken.state());
            boolean emptyToken = currToken.value().isEmpty() || nextToken.value().isEmpty();
            boolean isMergeable = sameMergeableToken || emptyToken;
            if (isMergeable && currToken instanceof Word currWord && nextToken instanceof Word nextWord) {
                Token token = merge(currWord, nextWord);
                tokens.set(i, token);
                tokens.remove(i + 1);
                i--;
            }
        }
    }

}
