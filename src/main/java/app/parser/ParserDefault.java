package app.parser;

import app.AppContext;
import app.ast.AST;
import app.ast.RedirectOutputNode;
import app.lexer.token.Operator;
import app.lexer.token.Token;
import app.lexer.token.Tokens;
import app.lexer.token.Word;

import java.nio.file.Path;
import java.util.Iterator;

public record ParserDefault(AppContext appContext) implements Parser {

    private static Context handleOperator(Operator operator, Context context) {
        return switch (operator.type()) {
            case OUTPUT -> {
                int index = context.tokens().indexOf(operator);
                final var subTokens = Tokens.of(context.tokens().subList(index + 1).toArray(Token[]::new));
                if (subTokens.isEmpty() || !(context.tokens().getFirst() instanceof Word filePath)) {
                    throw new IllegalArgumentException("Expected file after '>'");
                }

                Path target = Path.of(filePath.value());
                context.ast(new RedirectOutputNode(context.ast(), target));
                context.tokens().remove(index + 1);
                yield context;
            }
        };
    }

    @Override
    public AST apply(Tokens tokens) {
        Context context = new Context(tokens);

        for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext(); ) {
            Token token = iterator.next();
            context = handleToken(token, context);
        }

        if (!context.currWords().isEmpty() && context.ast() == null) {
            context.ast(context.commandFactory().getCommand(context.currWords()));
        }

        return context.ast();
    }

    private Context handleToken(Token token, Context context) {
        switch (token) {
            case Word word -> context.currWords().add(word);
            case Operator operator -> {
                context = handleCommandEnd(context);
                context = handleOperator(operator, context);
            }
            default -> throw new IllegalStateException("Unexpected value: " + token);
        }
        return context;
    }

    private Context handleCommandEnd(Context context) {
        final var tokens = context.currWords();
        final var commandFactory = context.commandFactory();
        final var command = commandFactory.getCommand(tokens);
        final var ast = context.ast() == null ? command : context.ast();
        context.ast(ast);
        tokens.clear();
        return context;
    }
}
