package com.jazzteam.cleopatra.component.util;

import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import javax.swing.*;

import static com.jazzteam.cleopatra.component.util.ImageUtil.getIcon;


public final class MessagesUtil {

    private MessagesUtil() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.PLAIN_MESSAGE, getIcon("snake.png"));
    }
}
