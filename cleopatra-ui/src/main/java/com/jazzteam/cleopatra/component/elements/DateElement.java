package com.jazzteam.cleopatra.component.elements;

import com.github.lgooddatepicker.components.DatePicker;
import com.google.common.base.Preconditions;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.function.Consumer;

public class DateElement extends FormElement {

    private DatePicker picker;
    protected Consumer<DateElement> validation;

    public DateElement(String label) {
        this(label, null, null);
    }

    public DateElement(String label, LocalDate initialDate) {
        this(label, initialDate, null);
    }

    public DateElement(String label, LocalDate initialDate, Consumer<DateElement> validation) {
        super(label);
        initDatePicker(initialDate);
        this.validation = validation;
    }

    @Override
    public JComponent createComponent(FormElementChangeListener changeListener) {
        picker.addDateChangeListener(changeEvent -> {
            if (changeListener != null)
                changeListener.onChange(this, picker, getParentForm());
            validate();
        });

        return picker;
    }

    @Override
    public void setEnabled(boolean enable) {
        picker.setEnabled(enable);
    }

    @Override
    public LocalDate getValue() {
        return picker.getDate();
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
        if (!(value instanceof LocalDate)) {
            throw new IllegalArgumentException(ErrorMessages.THE_GIVEN_VALUE_HAS_TO_BE_DATE.getMessage());
        }
        picker.setDate((LocalDate) value);
    }

    private void initDatePicker(LocalDate initialDate) {
        picker = new DatePicker();
        picker.setDate(initialDate);
    }

    public void setToolTip(String tipText) {
        Preconditions.checkNotNull(tipText);

        picker.getComponentDateTextField().addMouseListener(new MouseAdapter() {
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

        picker.getComponentDateTextField().addFocusListener(new FocusAdapter() {
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
        picker.getComponentDateTextField().setToolTipText(text);
    }

    private void hideTooltip() {
        picker.getComponentDateTextField().setToolTipText(null);
    }
}
