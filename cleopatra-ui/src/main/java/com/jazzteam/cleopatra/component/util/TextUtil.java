package com.jazzteam.cleopatra.component.util;

import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import static com.jazzteam.cleopatra.component.util.HtmlBuilder.*;

public final class TextUtil {

    private TextUtil() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static String setBoldAndUnderlined(String text) {
        return getAsHtml(makeBold(makeUnderlined(text(text, addLineBreak())))).toString();
    }

    public static String setBold(String text) {
        return getAsHtml(makeBold(text(text, addLineBreak()))).toString();
    }
}
