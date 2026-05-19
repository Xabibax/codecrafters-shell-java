package app.parser;

import app.AppContext;
import app.ast.AST;
import app.ast.RedirectOutputNode;
import app.lexer.token.Operator;
import app.lexer.token.Token;
import app.lexer.token.Tokens;
import app.lexer.token.Word;

import java.nio.file.Path;

public record ParserDefault(AppContext appContext) implements Parser {

    private static Context handleOperator(Operator operator, Context context) {
        return switch (operator.type()) {
            case OUTPUT -> {
                int index = context.words().indexOf(operator);
                if (index + 1 < context.words().size() && context.words().get(index + 1) instanceof Word filePath) {
                    Path target = Path.of(filePath.value());
                    context.ast(new RedirectOutputNode(context.ast(), target));
                    context.words().remove(index + 1);
                    yield context;
                } else {
                    throw new IllegalArgumentException("Expected file after '>'");
                }
            }
        };
    }

    @Override
    public AST apply(Tokens tokens) {
        Context context = new Context();

        for (var token : tokens) {
            context = handleToken(token, context);
        }

        if (!context.words().isEmpty()) {
            context.ast(context.commandFactory().getCommand(context.words()));

        }

        return context.ast();
    }

    private Context handleToken(Token token, Context context) {
        switch (token) {
            case Word word -> context.words().add(word);
            case Operator operator -> {
                context = handleCommandEnd(context);
                context = handleOperator(operator, context);
            }
            default -> throw new IllegalStateException("Unexpected value: " + token);
        }
        return context;
    }

    private Context handleCommandEnd(Context context) {
        final var tokens = context.words();
        final var commandFactory = context.commandFactory();
        final var command = commandFactory.getCommand(tokens);
        final var ast = context.ast() == null ? command : context.ast();
        context.ast(ast);
        tokens.clear();
        return context;
    }
}
