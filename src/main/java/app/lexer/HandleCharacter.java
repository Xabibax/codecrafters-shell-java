package app.lexer;

import app.token.Token;
import app.token.Token.State;

import static app.lexer.Lexer.State.*;

record HandleCharacter(Lexer.LexerContext context) {

    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '"';
    public static final char ESCAPE = '\\';
    public static final char SPACE = ' ';

    void handleSpace(char currentChar) {
        switch (currentChar) {
            case SPACE -> {
                context.tokenBuilder.state(Token.State.SPACE);
                context.tokenBuilder.append(currentChar);
            }
            case SINGLE_QUOTE -> {
                context.handleTokenEnd();
                context.state = SINGLE_QUOTES_OPEN;
                context.tokenBuilder.state(Token.State.SINGLE_QUOTED);
            }
            case DOUBLE_QUOTE -> {
                context.handleTokenEnd();
                context.state = DOUBLE_QUOTES_OPEN;
                context.tokenBuilder.state(State.DOUBLE_QUOTED);
            }
            case ESCAPE -> {
                context.handleTokenEnd();
                context.state = Lexer.State.ESCAPE;
            }
            default -> {
                context.handleTokenEnd();
                context.state = NORMAL;
                context.tokenBuilder.append(currentChar);
            }
        }
    }

    void handleEscape(char currentChar) {
        context.tokenBuilder.append(currentChar);
        context.state = NORMAL;
    }

    void handleDoubleQuotesOpen(char currentChar) {
        if (DOUBLE_QUOTE == currentChar) {
            context.state = DOUBLE_QUOTES_CLOSE;
        } else {
            context.state = DOUBLE_QUOTES_OPEN;
            context.tokenBuilder.append(currentChar);
        }
    }

    void handleDoubleQuotesClose(char currentChar) {
        switch (currentChar) {
            case DOUBLE_QUOTE -> context.state = DOUBLE_QUOTES_OPEN;
            case SINGLE_QUOTE -> {
                context.handleTokenEnd();
                context.tokenBuilder.state(State.SINGLE_QUOTED);
                context.state = Lexer.State.SINGLE_QUOTES_OPEN;
            }
            case SPACE -> {
                context.handleTokenEnd();
                context.tokenBuilder.append(currentChar);
                context.tokenBuilder.state(State.SPACE);
                context.state = Lexer.State.SPACE;
            }
            case ESCAPE -> {
                context.handleTokenEnd();
                context.state = Lexer.State.ESCAPE;
            }
            default -> {
                context.handleTokenEnd();
                context.tokenBuilder.append(currentChar);
                context.state = NORMAL;
            }
        }
    }

    void handleSingleQuotesOpen(char currentChar) {
        if (SINGLE_QUOTE == currentChar) {
            context.state = SINGLE_QUOTES_CLOSE;
        } else {
            context.state = SINGLE_QUOTES_OPEN;
            context.tokenBuilder.append(currentChar);
        }
    }

    void handleSingleQuotesClose(char currentChar) {
        switch (currentChar) {
            case SINGLE_QUOTE -> context.state = SINGLE_QUOTES_OPEN;
            case DOUBLE_QUOTE -> {
                context.handleTokenEnd();
                context.tokenBuilder.state(State.DOUBLE_QUOTED);
                context.state = DOUBLE_QUOTES_OPEN;
            }
            case SPACE -> {
                context.handleTokenEnd();
                context.tokenBuilder.append(currentChar);
                context.tokenBuilder.state(State.SPACE);
                context.state = Lexer.State.SPACE;
            }
            case ESCAPE -> {
                context.handleTokenEnd();
                context.state = Lexer.State.ESCAPE;
            }
            default -> {
                context.handleTokenEnd();
                context.tokenBuilder.append(currentChar);
                context.state = NORMAL;
            }
        }
    }

    void handleNormal(char currentChar) {
        switch (currentChar) {
            case SINGLE_QUOTE -> {
                context.handleTokenEnd();
                context.tokenBuilder.state(Token.State.SINGLE_QUOTED);
                context.state = Lexer.State.SINGLE_QUOTES_OPEN;
            }
            case DOUBLE_QUOTE -> {
                if (context.isEscape()) {
                    context.tokenBuilder.append(currentChar);
                    context.setEscape(false);
                    return;
                }
                context.handleTokenEnd();
                context.tokenBuilder.state(Token.State.DOUBLE_QUOTED);
                context.state = Lexer.State.DOUBLE_QUOTES_OPEN;
            }
            case ESCAPE -> {
                if (context.isEscape()) {
                    context.tokenBuilder.append(currentChar);
                    context.setEscape(false);
                    return;
                }
                context.state = Lexer.State.ESCAPE;
                context.setEscape(true);
            }
            case SPACE -> {
                if (context.isEscape()) {
                    context.tokenBuilder.append(currentChar);
                    context.setEscape(false);
                    return;
                }
                if (context.tokenBuilder.isNonEmpty()) {
                    context.handleTokenEnd();
                }
                context.tokenBuilder.state(Token.State.SPACE);
                context.tokenBuilder.append(currentChar);
                context.state = Lexer.State.SPACE;
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
            case ESCAPE -> handleEscape(currentChar);
            case SPACE -> handleSpace(currentChar);
        }
    }
}
