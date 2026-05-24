package app.lexer;

import app.models.token.Token;
import app.models.token.TokenBuilder;
import app.models.token.Tokens;
import lombok.Getter;
import lombok.Setter;

import static app.lexer.State.NORMAL;

@Setter
@Getter
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
        if(tokenBuilder.isNonEmpty()){
            Token token = tokenBuilder.build();
            tokens.add(token);
        }
        tokenBuilder.reset();
        switch (state) {
            case NORMAL, SPACE -> state = State.NORMAL;
            case SINGLE_QUOTES_OPEN, SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_OPEN, DOUBLE_QUOTES_CLOSE -> {
            }
        }
    }

    public void handleWordPartEnd() {
        Token token = tokenBuilder.build();
        tokens.add(token);
        tokenBuilder.reset();
        switch (state) {
            case NORMAL, SPACE -> state = State.NORMAL;
            case SINGLE_QUOTES_OPEN, SINGLE_QUOTES_CLOSE, DOUBLE_QUOTES_OPEN, DOUBLE_QUOTES_CLOSE -> {
            }
        }
    }

    public boolean isAtEnd() {
        return input.length() <= pos;
    }

    public int remainingChar() {
        return input.length() - pos;
    }

    public char nextChar() {
        return input.charAt(pos++);
    }

    public String toString() {
        String left = pos == 0 ? "" : input.substring(0, pos);
        String curr = String.valueOf(input.charAt(pos));
        String right= pos == input.length() - 1 ? "" : input.substring(pos + 1);
        return  "%s[%s]%s".formatted(left,curr,right);
    }
}
