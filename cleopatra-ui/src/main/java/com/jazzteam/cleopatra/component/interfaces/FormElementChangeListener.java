package com.jazzteam.cleopatra.component.interfaces;

import com.jazzteam.cleopatra.component.base.Form;
import com.jazzteam.cleopatra.component.base.FormElement;

@FunctionalInterface
public interface FormElementChangeListener {
    void onChange(FormElement element, Object value, Form form);
}
