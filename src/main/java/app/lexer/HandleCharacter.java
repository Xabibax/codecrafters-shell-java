package app.lexer;

import app.models.token.Type;

import static app.lexer.State.*;

record HandleCharacter(Context context) {

    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '"';
    public static final char ESCAPE = '\\';
    public static final char SPACE = ' ';
    public static final char REDIRECT_OUTPUT = '>';

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
            case REDIRECT_OUTPUT -> {
                context.handleTokenEnd();
                context.tokenBuilder.value(">")
                        .type(Type.OPERATOR)
                        .setNormalState()
                ;
                context.state = State.REDIRECT_OUTPUT;
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
            case REDIRECT_OUTPUT -> {
                context.tokenBuilder.value(">")
                        .type(Type.OPERATOR)
                        .setNormalState()
                ;
                context.state = State.REDIRECT_OUTPUT;
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
            case REDIRECT_OUTPUT -> {
                context.handleTokenEnd();
                context.tokenBuilder.value(">")
                        .type(Type.OPERATOR)
                        .setNormalState()
                ;
                context.state = State.REDIRECT_OUTPUT;
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
            case REDIRECT_OUTPUT -> {
                context.handleTokenEnd();
                context.tokenBuilder.value(">")
                        .type(Type.OPERATOR)
                        .setNormalState()
                ;
                context.state = State.REDIRECT_OUTPUT;
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
            case REDIRECT_OUTPUT -> handleRedirectOutput(currentChar);
        }
    }

    private void handleRedirectOutput(char currentChar) {
        if (context.isEscape()) {
            context.tokenBuilder.append(currentChar);
            context.setEscape(false);
            return;
        }
        switch (currentChar) {
            case SINGLE_QUOTE -> {
                context.handleTokenEnd();
                context.tokenBuilder.setSingleQuoteState();
                context.state = SINGLE_QUOTES_OPEN;
            }
            case DOUBLE_QUOTE -> {
                context.handleTokenEnd();
                context.tokenBuilder.setDoubleQuoteState();
                context.state = DOUBLE_QUOTES_OPEN;
            }
            case ESCAPE -> context.setEscape(true);
            case SPACE -> {
            }
            case REDIRECT_OUTPUT -> {
                context.handleTokenEnd();
                context.tokenBuilder.value(">")
                        .type(Type.OPERATOR)
                        .setNormalState()
                ;
                context.state = State.REDIRECT_OUTPUT;
            }
            default -> {
                context.handleTokenEnd();
                context.tokenBuilder.setNormalState();
                context.tokenBuilder.append(currentChar);
            }
        }
    }
}
