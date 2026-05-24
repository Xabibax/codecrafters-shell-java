package app.parser;

import app.models.ast.AST;
import app.models.ast.Redirect;
import app.models.ast.RedirectNode;
import app.models.token.operator.Operator;
import app.models.token.Token;
import app.models.token.Tokens;
import app.models.token.operator.RedirectErr;
import app.models.token.operator.RedirectOut;
import app.models.token.word.Word;
import app.models.token.word.Words;

import java.nio.file.Path;
import java.util.List;

public record ParserDefault() implements Parser {


    private void getRedirectAst(Redirect redirect, Context context) {
    }

    @Override
    public AST apply(Tokens tokens) {
        Context context = new Context(tokens);
        final var curWords = new Words();

        while (!context.isAtEnd()) {
            final var token = context.nextToken();
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
            case Word word -> handleWord(context, word);
            case Operator operator -> handleOperator(context, operator);
            default -> throw new IllegalStateException("Unexpected value: " + token);
        }
    }

    private void handleOperator(Context context, Operator operator) {
        handleCommandEnd(context);
        switch (operator) {
            case RedirectErr redirectErr -> {
                Token token = context.nextToken();
                if(! (token instanceof Word)) {
                    throw new IllegalArgumentException("A redirection excpect a word");
                }
                Redirect redirect = new Redirect(Redirect.RedirectSource.ERR,
                        Redirect.RedirectType.WRITE,
                        Path.of(token.value()));
                new RedirectNode(context.getAst(), List.of(redirect));
            }
            case RedirectOut redirectOut -> {
                Token token = context.nextToken();
                if(! (token instanceof Word)) {
                    throw new IllegalArgumentException("A redirection excpect a word");
                }
                Redirect redirect = new Redirect(Redirect.RedirectSource.OUT,
                        Redirect.RedirectType.WRITE,
                        Path.of(token.value()));
                new RedirectNode(context.getAst(), List.of(redirect));
            }
        }
    }

    private static boolean handleWord(Context context, Word word) {
        return context.currWords()
                .add(word);
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
