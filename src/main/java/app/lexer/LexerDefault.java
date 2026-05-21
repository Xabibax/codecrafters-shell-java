package app.lexer;

import app.models.token.Tokens;

public record LexerDefault() implements Lexer {

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

        if (!context.state.isTerminal()) {
            throw new IncoherentFinalStateException(context.state);
        }

        return context.tokens;
    }
}
