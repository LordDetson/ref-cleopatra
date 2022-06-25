package com.jazzteam.cleopatra.component.base;

import com.jazzteam.cleopatra.component.base.builders.PanelCreator;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;
import java.util.List;

public class RowFormElement extends FormElement {

    private final List<FormElement> elements;
    private final boolean isVertical;

    public RowFormElement(String label, List<FormElement> elements, boolean isVertical) {
        super(label);
        this.elements = elements;
        this.isVertical = isVertical;
    }

    @Override
    public JComponent createComponent(FormElementChangeListener onChange) {
        Box box;
        if (isVertical) {
            box = Box.createVerticalBox();
            box.setAlignmentY(Box.LEFT_ALIGNMENT);
        } else {
            box = Box.createHorizontalBox();
            box.setAlignmentY(Box.TOP_ALIGNMENT);
        }

        for (FormElement element : elements) {
            JPanel panel = PanelCreator.createPanel(element, onChange, 0, BoxLayout.X_AXIS);
            box.add(panel);
        }
        return box;
    }

    @Override
    public List<FormElement> getValue() {
        return elements;
    }

    @Override
    public boolean validate() {
        return elements.stream()
                .allMatch(FormElement::validate);
    }

    @Override
    public void setValue(Object value) {
        // do nothing
    }

    @Override
    public void setEnabled(boolean enable) {
        for (FormElement element : elements) {
            element.setEnabled(enable);
        }
    }

}
