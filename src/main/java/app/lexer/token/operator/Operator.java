package app.lexer.token.operator;

import app.ast.operator.Type;
import app.lexer.token.Token;

public class Operator extends Token {
    public Operator(String value, State state) {
        super(value, state);
    }

    public Operator(StringBuilder value, State state) {
        this(value.toString(), state);
    }

    public Type getType() {
        return Type.valueOf(value());
    }

}
