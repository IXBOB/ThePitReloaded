package net.ixbob.thepit;

import java.util.Arrays;

public class PitText<T> {

    protected final String text;
    protected final PitTextFormatter<T>[] formatters;

    @SafeVarargs
    public PitText(String text, PitTextFormatter<T>... formatters) {
        this.text = text;
        this.formatters = formatters;
    }

    public String getLegacyText() {
        return this.text;
    }

    public String getFormattedText(T applyPlaceholderObject) {
        return text.formatted(Arrays.stream(formatters)
                .map(f -> f.getFormattedString(applyPlaceholderObject))
                .toArray());
    }

}
