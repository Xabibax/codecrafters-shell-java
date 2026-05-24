package app.lexer;

import app.models.token.operator.Operator;
import app.models.token.operator.RedirectErr;
import app.models.token.operator.RedirectOut;

import java.util.Optional;

import static app.lexer.State.*;

record HandleCharacter(Context context) {

    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '"';
    public static final char ESCAPE = '\\';
    public static final char SPACE = ' ';

    void handleSpace(char currentChar) {
        switch (currentChar) {
            case SPACE -> {
            }
            case SINGLE_QUOTE -> {
                context.state = SINGLE_QUOTES_OPEN;
                context.tokenBuilder.setSingleQuoteState();
            }
            case DOUBLE_QUOTE -> {
                context.state = DOUBLE_QUOTES_OPEN;
                context.tokenBuilder.setDoubleQuoteState();
            }
            case ESCAPE -> {
                context.state = NORMAL;
                context.setEscape(true);
            }
            default -> {
                context.state = NORMAL;
                context.tokenBuilder.append(currentChar);
            }
        }
    }

    void handleDoubleQuotesOpen(char currentChar) {
        if (context.isEscape()) {
            context.tokenBuilder.append(currentChar);
            context.setEscape(false);
            return;
        }
        switch (currentChar) {
            case DOUBLE_QUOTE -> context.state = DOUBLE_QUOTES_CLOSE;
            case ESCAPE -> context.setEscape(true);
            default -> context.tokenBuilder.append(currentChar);
        }
    }

    void handleDoubleQuotesClose(char currentChar) {
        switch (currentChar) {
            case SINGLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setSingleQuoteState();
                context.state = SINGLE_QUOTES_OPEN;
            }
            case DOUBLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setDoubleQuoteState();
                context.state = DOUBLE_QUOTES_OPEN;
            }
            case SPACE -> {
                context.handleTokenEnd();
                context.state = State.SPACE;
            }
            case ESCAPE -> {
                context.setEscape(true);
                context.state = NORMAL;
            }
            default -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setNormalState();
                context.state = NORMAL;
                context.tokenBuilder.append(currentChar);
            }
        }
    }

    void handleSingleQuotesOpen(char currentChar) {
        if (currentChar == SINGLE_QUOTE) {
            context.state = SINGLE_QUOTES_CLOSE;
        } else {
            context.tokenBuilder.append(currentChar);
        }
    }

    void handleSingleQuotesClose(char currentChar) {
        switch (currentChar) {
            case SINGLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setSingleQuoteState();
                context.state = SINGLE_QUOTES_OPEN;
            }
            case DOUBLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setDoubleQuoteState();
                context.state = DOUBLE_QUOTES_OPEN;
            }
            case SPACE -> {
                context.handleTokenEnd();
                context.state = State.SPACE;
            }
            case ESCAPE -> {
                context.setEscape(true);
                context.state = NORMAL;
            }
            default -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setNormalState();
                context.state = NORMAL;
                context.tokenBuilder.append(currentChar);
            }
        }
    }

    void handleNormal(char currentChar) {
        if (context.isEscape()) {
            context.tokenBuilder.append(currentChar);
            context.setEscape(false);
            return;
        }
        switch (currentChar) {
            case SINGLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setSingleQuoteState();
                context.state = SINGLE_QUOTES_OPEN;
            }
            case DOUBLE_QUOTE -> {
                context.tokenBuilder.appendWordPart();
                context.tokenBuilder.setDoubleQuoteState();
                context.state = DOUBLE_QUOTES_OPEN;
            }
            case ESCAPE -> context.setEscape(true);
            case SPACE -> {
                context.handleTokenEnd();
                context.state = State.SPACE;
            }
            default -> context.tokenBuilder.append(currentChar);
        }
    }

    void handleChar(char currentChar) {
        switch (context.state) {
            case NORMAL -> handleNormal(currentChar);
            case SINGLE_QUOTES_OPEN -> handleSingleQuotesOpen(currentChar);
            case SINGLE_QUOTES_CLOSE -> handleSingleQuotesClose(currentChar);
            case DOUBLE_QUOTES_OPEN -> handleDoubleQuotesOpen(currentChar);
            case DOUBLE_QUOTES_CLOSE -> handleDoubleQuotesClose(currentChar);
            case SPACE -> handleSpace(currentChar);
        }
    }

    public Optional<Operator> handleOperator() {
        if(context.remainingChar() >= 2) {
            String substring = context.input.substring(context().pos, context().pos + 2);
            final Optional<Operator> operator = switch (substring) {
                case "1>" -> Optional.of(new RedirectOut(substring));
                case "2>" -> Optional.of(new RedirectErr(substring));
                default -> Optional.empty();
            };
            if (operator.isPresent()) {
                context.setPos(context.getPos() + 2);
                return operator;
            }
        }
        char charAt = context.input.charAt(context().pos);
        final Optional<Operator> operator = switch (charAt) {
            case '>' -> Optional.of(new RedirectOut(String.valueOf(charAt)));
            default -> Optional.empty();
        };
        if (operator.isPresent()){
            context.setPos(context.getPos() + 1);
        }
        return operator;
    }
}
