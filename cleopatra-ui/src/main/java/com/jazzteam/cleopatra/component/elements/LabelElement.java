package com.jazzteam.cleopatra.component.elements;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;

import static javax.swing.SwingConstants.CENTER;

public class LabelElement extends FormElement {

    private JLabel label;

    public LabelElement(String label) {
        super(null);
        initLabel(label);
    }

    @Override
    public JComponent createComponent(FormElementChangeListener onChange) {
        return label;
    }

    @Override
    public void setEnabled(boolean enable) {
        label.setEnabled(enable);
    }

    @Override
    public String getValue() {
        return label.getText();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void setValue(Object value) {
        label.setText(value.toString());
    }

    private void initLabel(String label) {
        this.label = new JLabel(label);
        this.label.setHorizontalAlignment(CENTER);
    }
}
