import app.Context;
import builtin.*;


void main() {
    Context context = new Context();
    while (true) {
        context.handleCommand();
    }
}