package com.jazzteam.cleopatra.component.base;

import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
@Setter
@Getter
public abstract class FormElement {

   private String id;
   private String label;
   private Form parentForm;

    protected boolean isValid;

    public FormElement(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public FormElement(String label) {
        this.label = label;
    }

    public abstract JComponent createComponent(FormElementChangeListener onChange);

    public abstract Object getValue();

    public boolean validate() {
        return true;
    }

    public abstract void setValue(Object value);

    public abstract void setEnabled(boolean enable);

    public Float asFloat() {
        return Float.parseFloat(getValue().toString());
    }

    public Integer asInt() {
        return Integer.parseInt(getValue().toString());
    }

    public String asString() {
        return getValue().toString();
    }

    public LocalDate asDate() {
        return (LocalDate) getValue();
    }
}
