package app.parser;

import app.models.ast.AST;
import app.models.ast.RedirectStdErrToFileNode;
import app.models.ast.RedirectStdOutToFileNode;
import app.models.token.operator.Operator;
import app.models.token.Token;
import app.models.token.Tokens;
import app.models.token.operator.Redirect;
import app.models.token.operator.RedirectStdErr;
import app.models.token.operator.RedirectStdOut;
import app.models.token.word.Word;

import java.nio.file.Path;

public record ParserDefault() implements Parser {

    private void handleOperator(Operator operator, Context context) {
        switch (operator) {
            case Redirect redirect -> getRedirectAst(redirect, context);
        }
    }

    private void getRedirectAst(Redirect redirect, Context context) {
        int index = context.tokens()
                .indexOf(redirect);
        final var subTokens = Tokens.of(context.tokens()
                .subList(index + 1)
                .toArray(Token[]::new));
        if (subTokens.isEmpty() || !(subTokens
                .getFirst() instanceof Word filePath)) {
            throw new IllegalArgumentException("Expected file after '>'");
        }

        Path target = Path.of(filePath.value());
        final var redirectAst = switch (redirect) {
            case RedirectStdOut _ -> new RedirectStdOutToFileNode(context.getAst(), target);
            case RedirectStdErr _ -> new RedirectStdErrToFileNode(context.getAst(), target);
        };
        context.setAst(redirectAst);
        context.tokens()
                .remove(index + 1);
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
