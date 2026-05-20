package app.lexer.token.operator;

import app.ast.operator.Type;
import app.lexer.token.Operator;
import app.lexer.token.State;

public record OperatorDefault(String value, State state) implements Operator {

    public OperatorDefault(StringBuilder value, State state) {
        this(value.toString(), state);
    }

    @Override
    public Type type() {
        return switch (value) {
            case ">" -> Type.OUTPUT;
            default -> throw new IllegalArgumentException("No operator found for: %s".formatted(value));
        };
    }

    @Override
    public String toString() {
        return "(%s, state: %s)".formatted(value, state);
    }
}
