package com.jazzteam.cleopatra.component.base.builders;

import com.jazzteam.cleopatra.component.base.Form;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.elements.*;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Setter
public class FormBuilder {
    protected final String title;
    protected final JFrame window;
    protected final List<FormElement> formElements;
    private FormElementChangeListener changeListener;

    private RowFormBuilder rowColumnFormBuilder;

    public FormBuilder(String title, JFrame window) {
        this.title = title;
        this.window = window;
        this.formElements = new LinkedList<>();
    }

    private void addElement(FormElement e) {
        formElements.add(e);
    }

    public FormBuilder addButton(String label, String buttonLabel, Icon icon, Runnable onClick, boolean isEnable, Predicate<FormElement> enableCondition) {
        ButtonElement button = new ButtonElement(label, buttonLabel, icon, onClick);
        button.setEnabled(isEnable);
        button.setEnableCondition(enableCondition);
        addElement(button);
        return this;
    }

    public FormBuilder addButton(String label, Icon icon, Runnable onClick) {
        addElement(new ButtonElement(label, null, icon, onClick));
        return this;
    }

    public FormBuilder addButton(String label, Icon icon, Runnable onClick, Action action) {
        addElement(new ButtonElement(label, null, icon, onClick, action));
        return this;
    }

    public FormBuilder addLabel(String label) {
        addElement(new LabelElement(label));
        return this;
    }

    public FormBuilder setChangeListener(FormElementChangeListener onChange) {
        this.changeListener = onChange;
        return this;
    }

    public FormBuilder addButton(Icon icon, Runnable onClick, Predicate<FormElement> enableCondition) {
        return addButton(null, null, icon, onClick, true, enableCondition);
    }

    public FormBuilder addButton(Icon icon, Runnable onClick) {
        return addButton(null, null, icon, onClick, true, null);
    }

    public FormBuilder addButton(Icon icon, Runnable onClick, boolean isEnable) {
        return addButton(null, null, icon, onClick, isEnable, null);
    }

    public FormBuilder addButton(Icon icon, Runnable onClick, boolean isEnable, Predicate<FormElement> enableCondition) {
        return addButton(null, null, icon, onClick, isEnable, enableCondition);
    }

    public FormBuilder addDatePicker(String label) {
        addElement(new DateElement(label));
        return this;
    }

    public FormBuilder addDatePicker(String label, LocalDate initDate) {
        addElement(new DateElement(label, initDate));
        return this;
    }

    public FormBuilder addDatePicker(String label, LocalDate initDate, Consumer<DateElement> validation) {
        addElement(new DateElement(label, initDate, validation));
        return this;
    }

    public FormBuilder addDatePicker(String label, LocalDate initDate, Consumer<DateElement> validation, String tooltip) {
        final DateElement dateElement = new DateElement(label, initDate, validation);
        dateElement.setToolTip(tooltip);
        addElement(dateElement);
        return this;
    }

    public FormBuilder addMultipleSelection(String label, String... elements) {
        addElement(new CheckBoxElementList(label, false, Arrays.asList(elements)));
        return this;
    }

    public FormBuilder addTextArea(String label) {
        addElement(new TextAreaElement(label, 3, "", false));
        return this;
    }


    public FormBuilder addTextArea(String label, String initialText, boolean readonly) {
        addElement(new TextAreaElement(label, 3, initialText, readonly));
        return this;
    }

    public FormBuilder addTextArea(String label, int rows) {
        addElement(new TextAreaElement(label, rows, "", false));
        return this;
    }

    public FormBuilder addSelection(String label, List<? extends Object> possibilities) {
        addElement(new SelectionElement<>(label, possibilities, null));
        return this;
    }

    public FormBuilder addSelection(String label, List<? extends Object> possibilities, Consumer<SelectionElement> validationConsumer) {
        addElement(new SelectionElement<>(label, possibilities, validationConsumer));
        return this;
    }

    public FormBuilder addCustomElement(FormElement element) {
        addElement(element);
        return this;
    }

    public FormBuilder addText(String label, Consumer<FormElement> textElementConsumer) {
        addElement(new TextElement(label, null, false, textElementConsumer));
        return this;
    }


    public FormBuilder addText(String label, String initialText, Consumer<FormElement> textElementConsumer) {
        addElement(new TextElement(label, initialText, false, textElementConsumer));
        return this;
    }

    public FormBuilder addText(String label, String initialText, boolean readonly, Consumer<FormElement> textElementConsumer) {
        addElement(new TextElement(label, initialText, readonly, textElementConsumer));
        return this;
    }

    public FormBuilder addText(String label, String initialText, boolean readonly, Consumer<FormElement> textElementConsumer, String tooltip) {
        final TextElement textElement = new TextElement(label, initialText, readonly, textElementConsumer);
        textElement.setToolTip(tooltip);
        addElement(textElement);
        return this;
    }

    public FormBuilder addText(String label, String initialText, boolean readonly, Consumer<FormElement> textElementConsumer, String tooltip, Predicate<FormElement> tooltipCondition) {
        final TextElement textElement = new TextElement(label, initialText, readonly, textElementConsumer);
        textElement.setToolTip(tooltip, tooltipCondition);
        addElement(textElement);
        return this;
    }

    public FormBuilder addTable(String label, List<String> header, List<String> bindEntityField, List<? extends Object> data, boolean isEditable) {
        final TableElement<?> tableElement = new TableElement<>(label, header, bindEntityField, data, isEditable);

        addElement(tableElement);
        return this;
    }

    public FormBuilder addCheckbox(String headline, String label) {
        addElement(new CheckBoxElement(label, headline, null));

        return this;
    }

    public RowFormBuilder addRow() {
        return addRow(null, false);
    }

    public RowFormBuilder addRow(String label, boolean isVertical) {
        rowColumnFormBuilder = new RowFormBuilder(label, this, isVertical);
        return rowColumnFormBuilder;
    }

    public FormBuilder endRow() {
        formElements.add(rowColumnFormBuilder.getRowElement());
        return this;
    }

    public FormBuilder addTextArea(String label, String initialText, Consumer<FormElement> textValidation, boolean readonly) {
        addElement(new TextAreaElement(label, 3, initialText, textValidation, readonly));
        return this;
    }

    public FormBuilder addTextArea(String label, String initialText, Consumer<FormElement> textValidation, boolean readonly, String tooltip, boolean showTooltipAfterFocusLost) {
        final TextAreaElement areaElement = new TextAreaElement(label, 3, initialText, textValidation, readonly);

        if (showTooltipAfterFocusLost) {
            areaElement.setToolTipAfterFocusLost(tooltip);
        } else {
            areaElement.setToolTip(tooltip);
        }

        addElement(areaElement);
        return this;
    }

    public FormBuilder addTextArea(String label, String initialText, Consumer<FormElement> textValidation, boolean readonly, String tooltip) {
        return addTextArea(label, initialText, textValidation,readonly,tooltip, false);
    }

    public Form build() {
        final Form form = new Form(window, formElements);
        window.setTitle(title);
        JPanel panel = PanelCreator.createPanel(formElements, changeListener, 5, BoxLayout.Y_AXIS);
        form.getForm().add(panel);
        return form;
    }

}
