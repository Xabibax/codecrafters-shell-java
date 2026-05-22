package app.parser;

import app.models.ast.AST;
import app.models.ast.RedirectOutputNode;
import app.models.token.operator.Operator;
import app.models.token.Token;
import app.models.token.Tokens;
import app.models.token.operator.Output;
import app.models.token.word.Word;

import java.nio.file.Path;

public record ParserDefault() implements Parser {

    private void handleOperator(Operator operator, Context context) {
        switch (operator) {
            case Output _ -> {
                int index = context.tokens()
                        .indexOf(operator);
                final var subTokens = Tokens.of(context.tokens()
                        .subList(index + 1)
                        .toArray(Token[]::new));
                if (subTokens.isEmpty() || !(subTokens
                        .getFirst() instanceof Word filePath)) {
                    throw new IllegalArgumentException("Expected file after '>'");
                }

                Path target = Path.of(filePath.value());
                context.setAst(new RedirectOutputNode(context.getAst(), target));
                context.tokens()
                        .remove(index + 1);
            }
        }
    }

    @Override
    public AST apply(Tokens tokens) {
        Context context = new Context(tokens);

        for (Token token : tokens) {
            handleToken(token, context);
        }

        if (!context.currWords()
                .isEmpty() && context.getAst() == null) {
            context.setAst(context.getCommandFactory()
                    .getCommand(context.currWords()));
        }

        return context.getAst();
    }

    private void handleToken(Token token, Context context) {
        switch (token) {
            case Word word -> context.currWords()
                    .add(word);
            case Operator operator -> {
                handleCommandEnd(context);
                handleOperator(operator, context);
            }
            default -> throw new IllegalStateException("Unexpected value: " + token);
        }
    }

    private void handleCommandEnd(Context context) {
        final var tokens = context.currWords();
        final var commandFactory = context.getCommandFactory();
        final var command = commandFactory.getCommand(tokens);
        final var ast = context.getAst() == null ? command : context.getAst();
        context.setAst(ast);
        tokens.clear();
    }
}
