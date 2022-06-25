package com.jazzteam.cleopatra.component.util;

import java.util.Arrays;

public class HtmlBuilder {

    private static final String STRONG_TAG = "strong";
    private static final String PARAGRAPH_TAG = "p";
    private static final String BREAK_LINE_TAG = "br";
    private static final String UNDERLINE_TAG = "u";
    private static final String BOLD_TAG = "b";
    private static final String HTML_TAG = "html";

    protected final String tag;
    private final HtmlBuilder[] items;

    public HtmlBuilder(String tag, final HtmlBuilder... items) {
        this.tag = tag;
        this.items = items;
    }

    public static HtmlBuilder text(String text, final HtmlBuilder... items) {
        return new HtmlBuilder(text, items) {

            @Override
            public String toString() {
                return tag;
            }

            @Override
            protected String buildString(StringBuilder builder) {
                return builder.append(tag).toString();
            }
        };
    }

    public static HtmlBuilder makeParagraph(final HtmlBuilder... items) {
        return new HtmlBuilder(PARAGRAPH_TAG, items);
    }

    public static HtmlBuilder getAsHtml(final HtmlBuilder... items) {
        return new HtmlBuilder(HTML_TAG, items);
    }

    public static HtmlBuilder makeBold(final HtmlBuilder... items) {
        return new HtmlBuilder(BOLD_TAG, items);
    }


    public static HtmlBuilder makeUnderlined(final HtmlBuilder... items) {
        return new HtmlBuilder(UNDERLINE_TAG, items);
    }

    public static HtmlBuilder addLineBreak(final HtmlBuilder... items) {
        return new HtmlBuilder(BREAK_LINE_TAG, items);
    }

    public static HtmlBuilder makeStrong(final HtmlBuilder... items) {
        return new HtmlBuilder(STRONG_TAG, items);
    }

    @Override
    public String toString() {
        return buildString(new StringBuilder());
    }

    protected String buildString(StringBuilder builder) {
        builder.append('<').append(tag);
        if (items.length == 0) {
            builder.append(" />");

        } else {
            builder.append('>');

            Arrays.stream(items).forEach(item -> item.buildString(builder));

            builder.append("</")
                    .append(tag)
                    .append('>');
        }
        return builder.toString();
    }
}