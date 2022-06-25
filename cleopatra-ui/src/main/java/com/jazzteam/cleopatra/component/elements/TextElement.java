package com.jazzteam.cleopatra.component.elements;

import com.google.common.base.Preconditions;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TextElement extends FormElement {
    private JTextField textfield;
    protected Consumer<FormElement> validation;

    public TextElement(String label, String initialText, boolean readonly, Consumer<FormElement> validation) {
        super(label);
        initTextField(initialText, readonly, validation);
    }

    @Override
    public JComponent createComponent(FormElementChangeListener changeListener) {
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (changeListener != null) {
                    super.keyReleased(e);
                    changeListener.onChange(TextElement.this, getValue(), getParentForm());
                }
                validate();
            }
        });
        return textfield;
    }

    @Override
    public void setEnabled(boolean enable) {
        textfield.setEnabled(enable);
    }

    @Override
    public String getValue() {
        return textfield.getText();
    }

    @Override
    public boolean validate() {
        if (validation == null) {
            return true;
        }

        validation.accept(this);
        return isValid;
    }

    @Override
    public void setValue(Object value) {
        textfield.setText(value.toString());
    }

    public void setToolTip(String tipText, Predicate<FormElement> condition) {
        Preconditions.checkNotNull(condition);

        textfield.addActionListener(e -> {
            if (condition.test(this)) {
                showTooltip(tipText);
            } else {
                hideTooltip();
            }
        });
    }

    public void setToolTip(String tipText) {
        Preconditions.checkNotNull(tipText);

        textfield.addMouseListener(new MouseAdapter() {
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
        textfield.setToolTipText(text);
    }

    private void hideTooltip() {
        textfield.setToolTipText(null);
    }

    private void initTextField(String initialText, boolean readonly, Consumer<FormElement> validation) {

        textfield = new JTextField(initialText);
        textfield.setEditable(!readonly);

        if (initialText != null) {
            textfield.setCaretPosition(initialText.length());
        }

        if (validation != null) {
            this.validation = validation;
        }
    }

}
