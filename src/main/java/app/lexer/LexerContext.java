package app.lexer;

import app.lexer.token.Token;
import app.lexer.token.TokenBuilder;
import app.lexer.token.Tokens;

import static app.lexer.LexerState.NORMAL;

public class LexerContext {
    String input;
    int pos;
    Tokens tokens = new Tokens();
    TokenBuilder tokenBuilder = Token.builder();
    LexerState state = NORMAL;
    boolean escape = false;

    LexerContext(String input) {
        this.input = input;
        this.pos = 0;
    }

    public void handleTokenEnd() {
        Token token = tokenBuilder.build();
        tokens.add(token);
        tokenBuilder.reset();
        switch (state) {
            case NORMAL, SPACE -> state = LexerState.NORMAL;
            case SINGLE_QUOTES_OPEN, SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_OPEN, DOUBLE_QUOTES_CLOSE -> {
            }
        }
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public TokenBuilder getTokenBuilder() {
        return tokenBuilder;
    }

    public void setTokenBuilder(TokenBuilder tokenBuilder) {
        this.tokenBuilder = tokenBuilder;
    }

    public LexerState getState() {
        return state;
    }

    public void setState(LexerState state) {
        this.state = state;
    }

    public Tokens tokens() {
        return this.tokens;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public boolean isEscape() {
        return escape;
    }

    public boolean isAtEnd() {
        return input.length() == pos;
    }

    /**
     * return the currentChar and increment position
     */
    public char nextChar() {
        return input.charAt(pos++);
    }
}
