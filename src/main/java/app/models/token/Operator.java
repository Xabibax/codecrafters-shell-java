package app.models.token;

import app.models.ast.operator.Type;

public non-sealed interface Operator extends Token {
    Type type();
}
