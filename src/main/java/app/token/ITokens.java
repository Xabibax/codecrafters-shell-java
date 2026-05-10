package app.token;

import app.lexer.Lexer;

import java.util.List;

public interface  ITokens<T extends Token> extends List<T> {

    default String tokensJoiner() {
        final var res = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            final var parameter = get(i);
            if(i == 0) {
                res.append(parameter.value());
                continue;
            }
            final var previousParameter = get(i-1);
            switch (parameter.state()) {
                case Token.State.NORMAL -> {
                    if(Token.State.NORMAL.equals(previousParameter.state())) {
                        res.append(" ");
                    }
                    res.append(parameter.value());
                }
                case Token.State.SINGLE_QUOTED, Token.State.DOUBLE_QUOTED -> res.append(parameter.value());
                case Token.State.SPACE -> res.append(" ");
            }
        }
        return res.toString().trim();
    }

    default void trim() {
        while (isEmpty() && Token.State.SPACE.equals(getFirst().state())) {
            removeFirst();
        }
        while (!isEmpty() && Token.State.SPACE.equals(getLast().state())) {
            removeLast();
        }
    }
}
