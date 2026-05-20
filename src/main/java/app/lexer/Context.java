package app.lexer;

import app.lexer.token.Token;
import app.lexer.token.TokenBuilder;
import app.lexer.token.Tokens;

import static app.lexer.State.NORMAL;

class Context {
    String input;
    int pos;
    Tokens tokens = new Tokens();
    TokenBuilder tokenBuilder = Token.builder();
    State state = NORMAL;
    boolean escape = false;

    Context(String input) {
        this.input = input;
        this.pos = 0;
    }

    public void handleTokenEnd() {
        Token token = tokenBuilder.build();
        tokens.add(token);
        tokenBuilder.reset();
        tokenBuilder.reset();
        switch (state) {
            case NORMAL, SPACE, REDIRECT_OUTPUT -> state = State.NORMAL;
            case SINGLE_QUOTES_OPEN, SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_OPEN, DOUBLE_QUOTES_CLOSE -> {
            }
        }
    }

    public void handleWordPartEnd() {
        Token token = tokenBuilder.build();
        tokens.add(token);
        tokenBuilder.reset();
        switch (state) {
            case NORMAL, SPACE, REDIRECT_OUTPUT -> state = State.NORMAL;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Tokens tokens() {
        return this.tokens;
    }

    public boolean isEscape() {
        return escape;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
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
