package app.lexer.token;

import app.ast.operator.Type;

public non-sealed interface Operator extends Token {
    Type type();
}
