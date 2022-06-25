package com.jazzteam.cleopatra.component.elements;

import com.google.common.base.Preconditions;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Predicate;

public class ButtonElement extends FormElement {

    private final String buttonLabel;
    private final Runnable onClick;

    private JButton button;

    public ButtonElement(String label, String buttonLabel, Icon icon, Runnable onClick, Action action) {
        super(label);
        initButton(buttonLabel, icon, action);
        this.buttonLabel = buttonLabel;
        this.onClick = onClick;
    }


    public ButtonElement(String label, String buttonLabel, Icon icon, Runnable onClick) {
        this(label, buttonLabel, icon, onClick, null);
    }

    @Override
    public JComponent createComponent(FormElementChangeListener onChange) {
        addListeners(actionEvent -> {
            if (onClick != null) {
                onClick.run();
            }

            if (onChange != null)
                onChange.onChange(ButtonElement.this, buttonLabel, getParentForm());
        });
        return button;
    }

    public void addListeners(ActionListener listener) {
        button.addActionListener(listener);
    }

    public void setActionCommand(String actionCommand) {
        button.setActionCommand(actionCommand);
    }

    @Override
    public Object getValue() {
        return button.getText();
    }

    @Override
    public void setValue(Object value) {
        button.setText((String) value);
    }

    @Override
    public void setEnabled(boolean enable) {
        button.setEnabled(enable);
    }

    public void setEnableCondition(Predicate<FormElement> enableCondition) {
        if (enableCondition==null){
            return;
        }

        addListeners(e -> {
            setEnabled(enableCondition.test(this));
        });
    }

    private void initButton(String buttonLabel, Icon icon, Action action) {
        button = new JButton(buttonLabel, icon);
        if (action != null) {
            button.setAction(action);
        }
    }

    public void setToolTip(String tipText, Predicate<FormElement> condition) {
        Preconditions.checkNotNull(condition);

        button.addActionListener(e -> {
            if (condition.test(this)) {
                showTooltip(tipText);
            } else {
                hideTooltip();
            }
        });
    }

    public void setToolTip(String tipText) {
        Preconditions.checkNotNull(tipText);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                showTooltip(tipText);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                hideTooltip();
            }
        });
    }

    private void showTooltip(String text) {
        button.setToolTipText(text);
    }

    private void hideTooltip() {
        button.setToolTipText(null);
    }
}
