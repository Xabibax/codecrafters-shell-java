package app.lexer.token;

import java.util.List;
import java.util.stream.Collectors;

public class TokensUtils {

    private TokensUtils() {
    }

    public static String tokensJoiner(List<Token> tokens) {
        return tokens.stream().map(Token::toString).collect(Collectors.joining(" "));
    }
}
