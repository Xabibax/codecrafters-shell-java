package app.models.token.operator;

import app.models.ast.operator.Type;
import app.models.token.Operator;

public record OperatorDefault(String value) implements Operator {

    public OperatorDefault(StringBuilder value) {
        this(value.toString());
    }

    @Override
    public Type type() {
        return switch (value) {
            case ">" -> Type.OUTPUT;
            default -> throw new IllegalArgumentException("No operator found for: %s".formatted(value));
        };
    }
}
