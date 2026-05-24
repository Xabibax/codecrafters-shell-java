package app.models.ast;

import java.nio.file.Path;

public record Redirect(RedirectSource source, RedirectType type, RedirectTarget target) {

    public Redirect(RedirectSource source, RedirectType type, Path target) {
        this(source, type, new RedirectToFile(target));
    }
    public Redirect(RedirectSource source, RedirectType type, String target) {
        this(source, type, Path.of(target));
    }

    public enum RedirectSource {
        OUT,
        ERR,
    }
    public enum RedirectType {
        WRITE
    }

    public sealed interface RedirectTarget permits RedirectToErr, RedirectToFile, RedirectToOut {}

    public record RedirectToFile(Path target) implements RedirectTarget{}
    public record RedirectToErr() implements RedirectTarget{}
    public record RedirectToOut() implements RedirectTarget{}
}
