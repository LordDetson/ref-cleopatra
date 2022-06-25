package com.jazzteam.cleopatra.component.base;

import com.jazzteam.cleopatra.component.interfaces.CloseListener;
import com.jazzteam.cleopatra.component.interfaces.OpenListener;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Form {

    @Getter
    private final JFrame form;
    @Getter
    private final List<FormElement> elements;
    private Consumer<JDialog> onDialogCreated;

    public Form(JFrame window, List<FormElement> elements) {
        form = window;
        this.elements = elements;
        getAllFormElements().forEach(element -> element.setParentForm(this));
    }

    public boolean isValid() {
        return elements.stream().allMatch(FormElement::validate);
    }

    public void show() {
        this.form.setVisible(true);
    }

    public Form addWindowListeners(CloseListener formCloseListener, OpenListener openlistener) {
        if (form != null) {
            form.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    if (formCloseListener != null)
                        formCloseListener.onClose(Form.this);
                    super.windowClosed(e);
                }

                @Override
                public void windowOpened(WindowEvent e) {
                    super.windowOpened(e);
                    if (openlistener != null) {
                        openlistener.onOpen();
                    }
                }
            });
        }
        return this;
    }

    private List<FormElement> getAllFormElements() {
        List<FormElement> allElements = new ArrayList<>();
        for (FormElement element : elements) {
            if (element instanceof RowFormElement) {
                allElements.addAll(((RowFormElement) element).getValue());
            } else {
                allElements.add(element);
            }
        }
        return allElements;
    }

}
