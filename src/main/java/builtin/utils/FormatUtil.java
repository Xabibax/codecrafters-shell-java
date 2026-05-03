package builtin.utils;

import app.Parameters;
import app.Parameters.Parameter;

public class FormatUtil {
    public static final char QUOTE = '\'';
    public static final char SPACE = ' ';
    public static final char TAB = '\t';

    /**
     * Util method to manage single quotes in line
     * <p>
     * Characters inside single quotes (including escape characters and potential special characters like $, *, or ~)
     * lose their special meaning and are treated as normal characters.
     * <p>
     * Consecutive whitespace characters (spaces, tabs) inside single quotes are preserved
     * and are not collapsed or used as delimiters.
     * <p>
     * Quoted strings placed next to each other are concatenated to form a single argument.
     * <p>
     * @param line a raw {@code String} of the command line
     * @return An object that encapsulate the parameters of the command line (the first parameter is the command label)
     */
    public static Parameters format(String line) throws OddNumberSingleQuotesException {
        final var nbOfQuotes = line.chars().filter(i -> QUOTE == i).count();
        final var nbOfQuotesIsOdd = nbOfQuotes % 2 == 1;
        if (nbOfQuotesIsOdd) {
            throw new OddNumberSingleQuotesException("(%s) contains %s quotes. Quotes must be in even number.".formatted(line, nbOfQuotes));
        }

        line = line.replace("''", "");


        final var parameters = new Parameters();

        var parameterBuilder = Parameter.builder();

        var isBetweenQuotes = false;
        var isPreviousCharSpace = false;
        for (final var c : line.chars().toArray()) {
            final var isQuote = QUOTE == c;
            final var isSpace = SPACE == c || TAB == c;
            final var shouldSkipSpace = isPreviousCharSpace && isSpace && !isBetweenQuotes;
            if (shouldSkipSpace) {
                final var parameter = parameterBuilder.build();
                parameterBuilder = Parameter.builder();
                if (parameter.isPresent()) {
                    parameters.add(parameter);
                }
                continue;
            }

            if (isQuote) {
                isBetweenQuotes = !isBetweenQuotes;
                final var parameter = parameterBuilder.build();
                parameterBuilder = Parameter.builder();
                parameters.add(parameter);
                continue;
            }

            parameterBuilder.value().append(Character.toString(c));

            isPreviousCharSpace = isSpace;
        }

        final var parameter = parameterBuilder.build();
        if (parameter.isPresent()) {
            parameters.add(parameter);
        }

        return parameters;
    }

    public static class OddNumberSingleQuotesException extends Exception {
        public OddNumberSingleQuotesException(String message) {
            super(message);
        }
    }
}
