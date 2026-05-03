package app.token;

public class Operator extends Token {
    public Operator(String value, State state) {
        super(value, state);
    }

    public Operator(StringBuilder value, State state) {
        this(value.toString(), state);
    }
}
