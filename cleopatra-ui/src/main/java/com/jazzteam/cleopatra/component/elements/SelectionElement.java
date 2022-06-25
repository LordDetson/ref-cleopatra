package com.jazzteam.cleopatra.component.elements;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectionElement<T> extends FormElement {

    private List<T> possibilities;
    private final JComboBox<T> box = new JComboBox<>();
    private final Consumer<SelectionElement> validation;

    public SelectionElement(String label, List<T> possibilities, Consumer<SelectionElement> validationConsumer) {
        super(label);
        this.possibilities = possibilities;
        this.validation = validationConsumer;
    }

    @Override
    public JComponent createComponent(FormElementChangeListener changeListener) {
        box.setModel(new DefaultComboBoxModel<T>((T[]) possibilities.toArray()));
        box.addItemListener(itemEvent -> {
            if (changeListener != null) {
                changeListener.onChange(SelectionElement.this, getValue(), getParentForm());
            }
            validate();
        });

        return box;
    }

    @Override
    public void setEnabled(boolean enable) {
        box.setEnabled(enable);
    }

    @Override
    public T getValue() {
        return (T) box.getSelectedItem();
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
        box.setSelectedItem(value);
    }

    public void addSelection(T element) {
        possibilities = new ArrayList<>(possibilities);
        possibilities.add(element);
        box.addItem(element);
    }

    public List<T> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(List<T> possibilities) {
        this.possibilities = possibilities;
        box.removeAllItems();
        possibilities.forEach(box::addItem);
    }
}
