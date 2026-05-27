package app.models.ast;

import app.models.token.operator.RedirectErr;
import app.models.token.operator.RedirectOut;
import org.jspecify.annotations.NonNull;

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public record Redirect(
        RedirectSource source,
        RedirectType type,
        RedirectTarget target
) {

    public Redirect(RedirectSource source, RedirectType type, Path target) {
        this(source, type, new RedirectToFile(target));
    }

    public Redirect(RedirectOut redirect, Path target) {
        this(RedirectSource.OUT, RedirectType.from(redirect), new RedirectToFile(target));
    }

    public Redirect(RedirectErr redirect, Path target) {
        this(RedirectSource.ERR, RedirectType.from(redirect), new RedirectToFile(target));
    }

    public Redirect(RedirectSource source, RedirectType type, String target) {
        this(source, type, Path.of(target));
    }

    public OpenOption getOpenOptions() {
        return switch (type()) {
            case WRITE -> StandardOpenOption.WRITE;
            case APPEND -> StandardOpenOption.APPEND;
        };
    }

    public enum RedirectSource {
        OUT, ERR,
    }

    public enum RedirectType {
        WRITE, APPEND,
        ;

        private static @NonNull RedirectType getRedirectType(String redirect) {
            return switch (redirect) {
                case ">", "1>", "2>" -> RedirectType.WRITE;
                case ">>", "1>>", "2>>" -> RedirectType.APPEND;
                default -> throw new IllegalArgumentException(redirect);
            };
        }

        static RedirectType from(RedirectOut redirect) {
            return getRedirectType(redirect.value());
        }


        static RedirectType from(RedirectErr redirect) {
            return getRedirectType(redirect.value());
        }

    }

    public sealed interface RedirectTarget permits RedirectToErr, RedirectToFile, RedirectToOut {
    }

    public record RedirectToFile(Path target) implements RedirectTarget {
    }

    public record RedirectToErr() implements RedirectTarget {
    }

    public record RedirectToOut() implements RedirectTarget {
    }
}
