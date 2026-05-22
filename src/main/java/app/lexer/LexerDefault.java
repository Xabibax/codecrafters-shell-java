package app.lexer;

import app.models.token.Tokens;
import app.models.token.operator.Operator;

import java.util.Optional;

public record LexerDefault() implements Lexer {

    @Override
    public Tokens apply(String input) throws IncoherentFinalStateException {
        final var context = new Context(input);
        final var handleCharacter = new HandleCharacter(context);
        if (context.isAtEnd()) {
            return context.tokens;
        }

        while (!context.isAtEnd()) {
            if (!State.DOUBLE_QUOTES_OPEN.equals(context.state)
                    && !State.SINGLE_QUOTES_OPEN.equals(context.state)
                    && !context.isEscape()) {
                Optional<Operator> operator = handleCharacter.handleOperator();
                if(operator.isPresent()) {
                    context.handleTokenEnd();
                    context.tokens.add(operator.get());
                    continue;
                }
            }

            handleCharacter.handleChar(context.nextChar());
        }

        if (context.tokenBuilder.isNonEmpty()) {
            final var lastToken = context.tokenBuilder. build();
            context.tokens.add(lastToken);
        }

        if (!context.state.isTerminal()) {
            throw new IncoherentFinalStateException(context.state);
        }

        return context.tokens;
    }
}
