package app.models.token.operator;

public sealed interface Redirect extends Operator permits RedirectStdErr, RedirectStdOut {
}
