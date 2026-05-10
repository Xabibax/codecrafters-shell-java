package app.token;

import app.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface  ITokens<TOKEN extends Token, TOKENS extends List<TOKEN>> extends List<TOKEN> {

    default String tokensJoiner() {
        final List<String> res = new ArrayList<>();
        int size = size();
        for (int i = 0; i < size; i++) {
            final var parameter = get(i);
            if (i == 0) {
                res.add(parameter.value());
                continue;
            }
            final var previousParameter = get(i - 1);
            switch (parameter.state()) {
                case Token.State.NORMAL -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SPACE -> res.add(parameter.value());
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> res.set(res.size() - 1, res.getLast() + parameter.value());
                    }
                }
                case Token.State.SINGLE_QUOTED, Token.State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED ->
                                res.set(res.size() - 1, res.getLast() + parameter.value());
                        case SPACE -> res.add(parameter.value());
                    }
                }
                case Token.State.SPACE -> {
                }
            }
        }
        return String.join(" ", res);
    }

    TOKENS trim();
}
