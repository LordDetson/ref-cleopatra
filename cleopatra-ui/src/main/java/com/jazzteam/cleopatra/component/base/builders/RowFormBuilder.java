package com.jazzteam.cleopatra.component.base.builders;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.base.RowFormElement;

public class RowFormBuilder extends FormBuilder {
    private final FormBuilder parent;
    private final boolean isVertical;

    public RowFormBuilder(String title, FormBuilder parent, boolean isVertical) {
        super(title, parent.window);
        this.parent = parent;
        this.isVertical = isVertical;
    }

    public FormElement getRowElement() {
        return new RowFormElement(title, formElements, isVertical);
    }

    @Override
    public FormBuilder endRow() {
        parent.formElements.add(getRowElement());
        return parent;
    }
}
