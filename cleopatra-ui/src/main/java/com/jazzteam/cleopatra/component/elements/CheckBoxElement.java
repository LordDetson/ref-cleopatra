package com.jazzteam.cleopatra.component.elements;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CheckBoxElement extends FormElement {

    private final JLabel headline;
    private final JCheckBox checkbox;

    protected Consumer<CheckBoxElement> validation;

    public CheckBoxElement(String label) {
        this(label, null, null);
    }

    public CheckBoxElement(String label, String headline, Consumer<CheckBoxElement> validation) {
        super(headline);
        this.headline = new JLabel(label);
        this.validation = validation;
        this.checkbox = new JCheckBox();
    }

    @Override
    public JComponent createComponent(FormElementChangeListener onChange) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(headline, BorderLayout.WEST);
        panel.add(checkbox, BorderLayout.EAST);

        checkbox.addActionListener(actionEvent -> {
            if (onChange != null)
                onChange.onChange(this, checkbox, getParentForm());
            validate();
        });

        return panel;
    }

    @Override
    public void setEnabled(boolean enable) {
        headline.setEnabled(enable);
        checkbox.setEnabled(enable);
    }

    @Override
    public Object getValue() {
        return checkbox.isSelected();
    }

    @Override
    public boolean validate() {
        if (validation != null) {
            validation.accept(this);
        } else return true;

        return isValid;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Boolean) {
            checkbox.setSelected((Boolean) value);
        } else {
            throw new IllegalArgumentException(ErrorMessages.HAS_TO_BE_BOOLEAN.getMessage());
        }
    }

}
