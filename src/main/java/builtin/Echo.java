package builtin;

import app.Context;

import java.util.Arrays;
import java.util.function.Function;

public record Echo(Context context) implements Function<String, Integer> {

    public static final char QUOTE = '\'';
    public static final char SPACE = ' ';
    public static final char TAB = '\t';

    @Override
    public Integer apply(String line) {
        final var parameters = Arrays.stream(line.split(" ")).skip(1).toList();
        String message = String.join(" ", parameters);
        final var nbOfQuotes = message.chars().filter(i -> QUOTE == i).count();
        final var nbOfQuotesIsOdd = nbOfQuotes % 2 == 1;
        if(nbOfQuotesIsOdd) {
            System.err.println("(%s) contains %s quotes. Quotes must be in even number.".formatted(message, nbOfQuotes));
            return Context.FAIL;
        }

        message = message.replace("''","");


        final var sb = new StringBuilder();

        var isBetweenQuotes = false;
        var isPreviousCharSpace = false;
        for (final var c : message.chars().toArray()){
            final var isQuote = QUOTE == c;
            final var isSpace = SPACE == c || TAB == c;
            final var shouldSkipSpace = isPreviousCharSpace && isSpace && !isBetweenQuotes;
            if(shouldSkipSpace) {
                continue;
            }

            if(isQuote) {
                isBetweenQuotes = !isBetweenQuotes;
                continue;
            }

            sb.append(Character.toString(c));

            isPreviousCharSpace = isSpace;
        }

        IO.println(sb.toString());
        return Context.SUCCESS;
    }
}
