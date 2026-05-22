package app.models.token;


public interface Token {
    static TokenBuilder builder() {
        return new TokenBuilder();
    }

    String value();
}
