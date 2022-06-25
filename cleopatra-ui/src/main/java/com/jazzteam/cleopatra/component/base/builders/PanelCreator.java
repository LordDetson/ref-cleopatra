package com.jazzteam.cleopatra.component.base.builders;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public final class PanelCreator {

    private static final int TOP_MARGIN = 10;
    private static final int BOTTOM_MARGIN = 5;
    private static final int LEFT_MARGIN = 0;
    private static final int RIGHT_MARGIN = 0;

    private PanelCreator() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static JPanel createPanel(FormElement formElement, FormElementChangeListener changeListener, int border, int boxLayoutAxis) {
        return createPanel(Collections.singletonList(formElement), changeListener, border, boxLayoutAxis);
    }

    public static JPanel createPanel(List<FormElement> formElements, FormElementChangeListener changeListener, int border, int boxLayoutAxis) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(border, border, border, border));
        panel.setLayout(new BoxLayout(panel, boxLayoutAxis));

        for (FormElement formElement : formElements) {
            JPanel elementPanel = new JPanel(new BorderLayout());
            JComponent component = formElement.createComponent(changeListener);

            if (formElement.getLabel() != null) {
                JLabel label = new JLabel(formElement.getLabel());
                label.setBorder(new EmptyBorder(new Insets(TOP_MARGIN,LEFT_MARGIN,BOTTOM_MARGIN, RIGHT_MARGIN)));
                panel.add(label);
                elementPanel.add(label, BorderLayout.NORTH);
            }

            if (component != null) {
                elementPanel.add(component, BorderLayout.CENTER);
            }
            panel.add(elementPanel);
        }
        return panel;
    }

}
