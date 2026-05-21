package app.models.token;

import lombok.Getter;

@Getter
enum State {
    NORMAL(true),
    SINGLE_QUOTED(true),
    DOUBLE_QUOTED(true),
    ;

    final boolean isMergeable;

    State(boolean isMergeable) {
        this.isMergeable = isMergeable;
    }

}
