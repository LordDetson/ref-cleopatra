package com.jazzteam.cleopatra.component.util;

import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;

import static com.jazzteam.cleopatra.component.util.ImageUtil.getIcon;

@Slf4j
public final class SystemUiSettingsUtil {

    public enum Colors {
        DEFAULT_ORANGE(new Color(245, 121, 0)),
        DEFAULT_FOREGROUND(new Color(255, 255, 255)),
        DEFAULT_TEXT(new Color(19, 19, 24, 255));

        private final Color color;

        Colors(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    @Getter
    private static final String DEFAULT_ICON = "an.png";

    private static final BasicLookAndFeel DEFAULT_LOOK_AND_FEEL = new FlatArcOrangeIJTheme();

    private static final int FRAME_WIDTH = 850;
    private static final int FRAME_HEIGHT = 550;

    private SystemUiSettingsUtil() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static void setDefaultLookAndFeel() {
        setLookAndFeel(DEFAULT_LOOK_AND_FEEL);
    }

    public static void setLookAndFeel(BasicLookAndFeel newLookAndFeel) {
        try {
            UIManager.setLookAndFeel(newLookAndFeel);
            UIManager.put("Table.selectionBackground", new javax.swing.plaf.ColorUIResource(Colors.DEFAULT_ORANGE.getColor()));
            UIManager.put("Table.selectionBackgroundInactive", new javax.swing.plaf.ColorUIResource(Colors.DEFAULT_ORANGE.getColor()));

        } catch (UnsupportedLookAndFeelException e) {
            log.debug(ErrorMessages.INSTALL_LOOK_AND_FEEL_ERROR.getMessage() + e.getMessage());
        }
    }

    public static void updateLookAndFeel(Window frame, BasicLookAndFeel newLookAndFeel) {
        setLookAndFeel(newLookAndFeel);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public static void applySettingsForTable(JTable table) {
        table.setRowHeight(25);
        table.setSelectionBackground(Colors.DEFAULT_ORANGE.getColor());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setShowGrid(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void applySettingsForMainFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setIconImage(getIcon(DEFAULT_ICON).getImage());
        frame.setUndecorated(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    }
}
