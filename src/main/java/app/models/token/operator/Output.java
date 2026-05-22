package app.models.token.operator;

public record Output() implements Operator {
    @Override
    public String value() {
        return ">";
    }
}
