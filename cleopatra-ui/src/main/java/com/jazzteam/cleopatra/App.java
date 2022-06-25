package com.jazzteam.cleopatra;

import com.jazzteam.cleopatra.component.frames.MainTodoFrame;
import com.jazzteam.cleopatra.component.util.SystemUiSettingsUtil;

import javax.swing.*;

public class App {
    public static final String title = "Cleopatra";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SystemUiSettingsUtil.setDefaultLookAndFeel();

            MainTodoFrame mainFrame = new MainTodoFrame(title);
            mainFrame.showForm();
        });
    }

}
