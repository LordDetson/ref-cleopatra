package com.jazzteam.cleopatra.component.elements;

import com.google.common.base.Preconditions;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class TextAreaElement extends FormElement {

    private JTextArea area;
    private final Consumer<FormElement> validation;

    public TextAreaElement(String label, int rows, String initialText, boolean readOnly) {
        this(label, rows, initialText, null, readOnly);
    }

    public TextAreaElement(String label, int rows, String initialText, Consumer<FormElement> textElementConsumer, boolean readOnly) {
        super(label);
        this.validation = textElementConsumer;
        initTextArea(rows, initialText, readOnly);
    }

    @Override
    public JComponent createComponent(FormElementChangeListener changeListener) {
        area.addKeyListener(getKeyAdapter(changeListener));
        return new JScrollPane(area);
    }

    @Override
    public void setEnabled(boolean enable) {
        area.setEnabled(enable);
    }

    @Override
    public String getValue() {
        return area.getText();
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
        area.setText(value.toString());
    }

    private void initTextArea(int rows, String initialText, boolean readOnly) {
        this.area = new JTextArea(initialText);
        this.area.setEditable(!readOnly);
        this.area.setRows(rows);

    }

    public void setToolTip(String tipText) {
        Preconditions.checkNotNull(tipText);

        area.addMouseListener(new MouseAdapter() {
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

    public void setToolTipAfterFocusLost(String tipText) {
        Preconditions.checkNotNull(tipText);

        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hideTooltip();
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                showTooltip(tipText);
            }
        });
    }

    private void showTooltip(String text) {
        area.setToolTipText(text);
    }

    private void hideTooltip() {
        area.setToolTipText(null);
    }

    private KeyAdapter getKeyAdapter(FormElementChangeListener changeListener) {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (changeListener != null) {
                    changeListener.onChange(TextAreaElement.this, getValue(), getParentForm());
                }
                validate();
            }
        };
    }
}
