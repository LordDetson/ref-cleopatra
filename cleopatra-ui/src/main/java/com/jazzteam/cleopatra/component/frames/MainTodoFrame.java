package com.jazzteam.cleopatra.component.frames;


import com.jazzteam.cleopatra.component.base.Form;
import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.base.RowFormElement;
import com.jazzteam.cleopatra.component.base.builders.FormBuilder;
import com.jazzteam.cleopatra.component.elements.TableElement;
import com.jazzteam.cleopatra.component.util.SystemUiSettingsUtil;
import com.jazzteam.cleopatra.component.util.constant.ValidatorsType;
import com.jazzteam.cleopatra.component.util.constant.ViewMode;
import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.entity.Todo;
import com.jazzteam.cleopatra.service.impl.PriorityServiceImpl;
import com.jazzteam.cleopatra.service.impl.StatusServiceImpl;
import com.jazzteam.cleopatra.service.impl.TodoServiceImpl;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.jazzteam.cleopatra.component.util.ElementConsumerUtil.getFormElementConsumer;
import static com.jazzteam.cleopatra.component.util.ImageUtil.*;
import static com.jazzteam.cleopatra.component.util.TextUtil.setBold;
import static com.jazzteam.cleopatra.component.util.TextUtil.setBoldAndUnderlined;

public class MainTodoFrame {
    private static final String DELETE_TODO_MESSAGE = "Do you want to delete this Todo?";
    private static final String TOOLTIP = "Should be not empty and less than 100";
    private static final String DATE_TOOLTIP = "Start day should be early than end date";
    private static final String DELETE_TODO_TITLE = "Delete ToDo";

    private static final TodoServiceImpl todoService = new TodoServiceImpl();
    private static final PriorityServiceImpl priorityService = new PriorityServiceImpl();
    private static final StatusServiceImpl statusService = new StatusServiceImpl();

    private final JFrame frame = new JFrame();
    private final String title;

    private TableElement<Todo> todoTableElement;

    public MainTodoFrame(String title) {
        this.title = title;
        SystemUiSettingsUtil.applySettingsForMainFrame(frame, title);
    }

    public void showForm() {

        todoTableElement = initTodoTableElement();

        Form mainForm = new FormBuilder(title, frame)
                .addRow()
                .addButton(getIcon("new.png"), this::showCreatingTodoDialog)
                .addButton(getIcon("pen.png"), this::showUpdateTodoDialog, false, getEnableCondition())
                .addButton(getIcon("trash.png"), this::showDeletingDialog, false, getEnableCondition())
                .addButton(getIcon("arrow.png"), todoTableElement::moveUpwards, false, getEnableCondition())
                .addButton(new ImageIcon(createFlippedImage(getBufferedImage("arrow.png"))), todoTableElement::moveDownwards, false, element -> todoTableElement.isRowSelected())
                .addButton(getIcon("priority.png"), this::showPriorityDialog)
                .endRow()
                .addLabel("<html><br><br></html>")
                .addRow()
                .addLabel(setBold("TODO TASKS")).endRow()
                .addCustomElement(todoTableElement)
                .build();
        mainForm.show();
    }

    private void showPriorityDialog() {
        new PriorityDialog().showPriorityPanel();
        todoTableElement.load(todoService.getAll());
    }

    private void showDeletingDialog() {
        final int deleteToDo = JOptionPane.showConfirmDialog(null, DELETE_TODO_MESSAGE, DELETE_TODO_TITLE, JOptionPane.YES_NO_OPTION);
        if (deleteToDo == 0) {
            Todo todoForDelete = todoTableElement.listSelectedItem().get(0);
            todoService.delete(todoForDelete.getId());
            todoTableElement.removeRow();
        }
    }

    private Predicate<FormElement> getEnableCondition() {
        return element -> todoTableElement.isRowSelected();
    }

    private void showUpdateTodoDialog() {
        Todo todoForUpdate = todoTableElement.listSelectedItem().get(0);
        final Todo todo = todoService.get(todoForUpdate.getId());

        final JButton okButton = new JButton("Update");

        final JButton cancelButton = new JButton("Cancel");
        JButton[] options = new JButton[]{okButton, cancelButton};
        Form form = getFormTodo(todoForUpdate, okButton, ViewMode.UPDATE);

        JOptionPane optionPane = new JOptionPane();
        optionPane.setOptions(options);
        optionPane.setMessage(form.getForm().getContentPane());
        JDialog dialog = optionPane.createDialog(frame, "Update Todo");
        optionPane.setInitialValue(okButton);


        okButton.addActionListener(e -> {
            todoForUpdate.setCreateDate(todo.getCreateDate());

            initTodo(todoForUpdate, form);
            Todo updatedTodo = todoService.update(todoForUpdate.getId(), todoForUpdate);
            if (updatedTodo != null) {
                todoTableElement.addRow(updatedTodo);
                dialog.setVisible(false);
                todoTableElement.load(todoService.getAll());
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void showCreatingTodoDialog() {
        Todo todoForCreate = new Todo();

        final JButton okButton = new JButton("OK");
        okButton.setEnabled(false);

        final JButton cancelButton = new JButton("Cancel");
        JButton[] options = new JButton[]{okButton, cancelButton};

        Form form = getFormTodo(todoForCreate, okButton, ViewMode.NEW);


        JDialog dialog = initDialog(okButton, options, form);

        okButton.addActionListener(e -> {
            todoForCreate.setCreateDate(LocalDate.now());

            initTodo(todoForCreate, form);

            Todo createdTodo = todoService.create(todoForCreate);
            if (createdTodo != null) {
                todoTableElement.addRow(createdTodo);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.dispose();
        });
        dialog.setVisible(true);
    }

    private void initTodo(Todo todoForUpdate, Form form) {
        todoForUpdate.setTitle(String.valueOf(form.getElements().get(0).asString()));
        todoForUpdate.setDescription(form.getElements().get(1).asString());

        final RowFormElement rowFormElement = (RowFormElement) form.getElements().get(2);
        todoForUpdate.setPriority((Priority) rowFormElement.getValue().get(0).getValue());
        todoForUpdate.setStatus((Status) rowFormElement.getValue().get(1).getValue());

        todoForUpdate.setEndDate(form.getElements().get(3).asDate());
    }

    private JDialog initDialog(JButton okButton, JButton[] options, Form form) {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setOptions(options);
        optionPane.setMessage(form.getForm().getContentPane());
        JDialog dialog = optionPane.createDialog(frame, "Create Todo");
        optionPane.setInitialValue(okButton);
        return dialog;
    }

    private Form getFormTodo(Todo todo, JButton okButton, ViewMode viewMode) {
        final FormBuilder formBuilder = new FormBuilder(null, new JFrame())
                .setChangeListener((element, value, f) -> {
                    okButton.setEnabled(f.isValid());
                })
                .addText("Title",
                        todo.getTitle(),
                        false,
                        getFormElementConsumer(ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100),
                        TOOLTIP)
                .addTextArea("Description",
                        todo.getDescription(),
                        getFormElementConsumer(ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100),
                        false,
                        TOOLTIP)
                .addRow()
                .addSelection("Priority", priorityService.getAll())
                .addSelection("Status", statusService.getAll(), selectionElement -> {
                    selectionElement.setValid(selectionElement.getValue() != null);
                })
                .endRow();

        if (viewMode.equals(ViewMode.UPDATE)) {
            formBuilder.addDatePicker("Create date",
                    todo.getCreateDate(),
                    dateElement -> dateElement.setValid(dateElement.getValue() != null),
                    DATE_TOOLTIP);
        }

        formBuilder.addDatePicker("End date",
                todo.getEndDate(),
                dateElement -> dateElement.setValid(dateElement.getValue() != null),
                DATE_TOOLTIP);
        return formBuilder.build();
    }

    private TableElement<Todo> initTodoTableElement() {
        return new TableElement<>(null,
                List.of(setBoldAndUnderlined("TITLE"),
                        setBoldAndUnderlined("DESCRIPTION"),
                        setBoldAndUnderlined("PRIORITY"),
                        setBoldAndUnderlined("CREATED"),
                        setBoldAndUnderlined("DEADLINE"),
                        setBoldAndUnderlined("STATUS")),
                List.of("title", "description", "priority", "createDate", "endDate", "status"),
                todoService.getAll(), true);
    }

}
