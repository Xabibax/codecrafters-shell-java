package app.models.token;


public sealed interface Token permits Operator, Word {
    static TokenBuilder builder() {
        return new TokenBuilder();
    }

    String value();
}
