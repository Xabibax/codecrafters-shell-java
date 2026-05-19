package app.lexer.token;

import java.util.ArrayList;
import java.util.List;

public class TokensUtils {

    private TokensUtils() {
    }

    public static String tokensJoiner(List<Token> tokens) {
        final List<String> res = new ArrayList<>();
        int size = tokens.size();
        for (int i = 0; i < size; i++) {
            final var parameter = tokens.get(i);
            if (i == 0) {
                res.add(parameter.value());
                continue;
            }
            final var previousParameter = tokens.get(i - 1);
            switch (parameter.state()) {
                case State.NORMAL -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SPACE -> res.add(parameter.value());
                        case SINGLE_QUOTED, DOUBLE_QUOTED -> res.set(res.size() - 1, res.getLast() + parameter.value());
                    }
                }
                case State.SINGLE_QUOTED, State.DOUBLE_QUOTED -> {
                    switch (previousParameter.state()) {
                        case NORMAL, SINGLE_QUOTED, DOUBLE_QUOTED ->
                                res.set(res.size() - 1, res.getLast() + parameter.value());
                        case SPACE -> res.add(parameter.value());
                    }
                }
                case State.SPACE -> {
                }
            }
        }
        return String.join(" ", res);
    }
}
