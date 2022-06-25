package com.jazzteam.cleopatra.component.frames;

import com.jazzteam.cleopatra.component.base.Dialog;
import com.jazzteam.cleopatra.component.base.Form;
import com.jazzteam.cleopatra.component.base.builders.FormBuilder;
import com.jazzteam.cleopatra.component.elements.TableElement;
import com.jazzteam.cleopatra.component.util.ElementConsumerUtil;
import com.jazzteam.cleopatra.component.util.constant.ValidatorsType;
import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.service.PriorityService;
import com.jazzteam.cleopatra.service.TodoService;
import com.jazzteam.cleopatra.service.impl.PriorityServiceImpl;
import com.jazzteam.cleopatra.service.impl.TodoServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;

import static com.jazzteam.cleopatra.component.util.ImageUtil.*;
import static com.jazzteam.cleopatra.component.util.MessagesUtil.showMessage;
import static com.jazzteam.cleopatra.component.util.TextUtil.setBoldAndUnderlined;

@Slf4j
public class PriorityDialog {
    private static final PriorityService priorityService = new PriorityServiceImpl();
    private static final TodoService todoService = new TodoServiceImpl();
    private static final String REPLACE_ON_DEFAULT_PRIORItY_MESSAGE = "Would you like to set default priority in Todo with that priority? ";
    private static final String DELETE_MESSAGE = "Do you want to delete this priority?";
    private static final String DELETE_TITLE = "Delete Priority";
    private static final String NAME_LABEL = "NAME";
    private static final String WEIGHT_LABEL = "WEIGHT";

    void showPriorityPanel() {

        final TableElement<Priority> priorityTableElement = getPriorityTableElement();


        Form form = new FormBuilder("Priorities", new JFrame())
                .addRow()
                .addLabel("PRIORITY")
                .addText(NAME_LABEL, ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100))
                .addText(WEIGHT_LABEL, ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_NUMERIC_TEXT))
                .endRow()
                .addLabel("<html><br><br></html>")
                .addRow()
                .addButton(getIcon("new.png"), () -> {
                    Priority priorityForCreate = new Priority();

                    final JButton okButton = new JButton("Create");
                    okButton.setEnabled(false);
                    final JButton cancelButton = new JButton("Cancel");
                    JButton[] options = new JButton[]{okButton, cancelButton};

                    Form priorityForm = new FormBuilder(null, new JFrame())
                            .setChangeListener((element, value, f) -> {
                                okButton.setEnabled(f.isValid());
                            })
                            .addText("Name",
                                    null,
                                    false,
                                    ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100),
                                    "Should be not empty")
                            .addText("Weight",
                                    null, false,
                                    ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_NUMERIC_TEXT),
                                    "Should be numeric")
                            .build();

                    JOptionPane optionPane = new JOptionPane();
                    optionPane.setOptions(options);
                    optionPane.setMessage(priorityForm.getForm().getContentPane());

                    Dialog dialog = new Dialog(options, priorityForm.getForm().getContentPane());
                    optionPane.setInitialValue(okButton);

                    okButton.addActionListener(e -> {

                        priorityForCreate.setName(priorityForm.getElements().get(0).asString());
                        priorityForCreate.setWeight(priorityForm.getElements().get(1).asInt());

                        Priority updatedTodo = priorityService.create(priorityForCreate);
                        if (updatedTodo != null) {
                            priorityTableElement.addRow(updatedTodo);
                            priorityTableElement.load(priorityService.getAll());
                            dialog.dispose();
                        }
                    });

                    cancelButton.addActionListener(e -> {
                        dialog.dispose();
                    });
                    dialog.showDialog(null, "update", null, false);

                }, true)
                .addButton(getIcon("pen.png"), () -> {
                    if (!priorityTableElement.isRowSelected()) {
                        showMessage("Select Priority to move up");
                        return;
                    }
                    Priority priorityForUpdate = priorityTableElement.listSelectedItem().get(0);
                    final Priority todo = priorityService.get(priorityForUpdate.getId());

                    final JButton okButton = new JButton("Update");

                    final JButton cancelButton = new JButton("Cancel");
                    JButton[] options = new JButton[]{okButton, cancelButton};

                    Form priorityForm = new FormBuilder(null, new JFrame())
                            .setChangeListener((element, value, f) -> {
                                okButton.setEnabled(f.isValid());

                            })
                            .addText("Name", priorityForUpdate.getName(), ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100))
                            .addText("Weight", String.valueOf(priorityForUpdate.getWeight()), ElementConsumerUtil.getFormElementConsumer(ValidatorsType.NOT_NULL_NUMERIC_TEXT))
                            .build();


                    JOptionPane optionPane = new JOptionPane();
                    optionPane.setOptions(options);
                    optionPane.setMessage(priorityForm.getForm().getContentPane());
                    JDialog dialog = optionPane.createDialog(new JFrame(), "Update Priority");
                    optionPane.setInitialValue(okButton);


                    okButton.addActionListener(e -> {

                        priorityForUpdate.setName(priorityForm.getElements().get(0).asString());
                        priorityForUpdate.setWeight(priorityForm.getElements().get(1).asInt());

                        Priority updatedTodo = priorityService.update(priorityForUpdate.getId(), priorityForUpdate);
                        if (updatedTodo != null) {
                            priorityTableElement.addRow(updatedTodo);
                            dialog.setVisible(false);
                            priorityTableElement.load(priorityService.getAll());
                            dialog.dispose();
                        }
                    });

                    cancelButton.addActionListener(e -> {
                        dialog.setVisible(false);
                        dialog.dispose();
                    });

                    dialog.setVisible(true);
                }, true)
                .addButton(getIcon("trash.png"), () -> {
                    if (!priorityTableElement.isRowSelected()) {
                        showMessage("Select Priority to delete");
                        return;
                    }

                    final int deleteToDo = JOptionPane.showConfirmDialog(null, DELETE_MESSAGE, DELETE_TITLE, JOptionPane.YES_NO_OPTION);
                    if (deleteToDo == 0) {
                        Priority priorityForDelete = priorityTableElement.listSelectedItem().get(0);

                        try {
                            if (todoService.getFirstByPriority(priorityForDelete.getId()) != null) {
                                final int confirmDialog = JOptionPane.showConfirmDialog(null, REPLACE_ON_DEFAULT_PRIORItY_MESSAGE,
                                        DELETE_TITLE, JOptionPane.YES_NO_OPTION);
                                if (confirmDialog == 0) {
                                    todoService.getByPriority(priorityForDelete.getId()).parallelStream()
                                            .forEach(todo -> {
                                                todo.setPriority(priorityService.getDefault());
                                                todoService.update(todo.getId(), todo);
                                            });
                                } else {
                                    return;
                                }
                            }
                        } catch (Exception ex) {
                            log.debug("Todo with priority(" + priorityForDelete.getName() + ") not Found: " + ex.getMessage());
                        }

                        priorityService.delete(priorityForDelete.getId());
                        priorityTableElement.removeRow();
                    }
                }, true)

                .addButton(getIcon("arrow.png"), () -> {
                    if (!priorityTableElement.isRowSelected()) {
                        showMessage("Select Priority to move down");
                        return;
                    }
                    priorityTableElement.moveUpwards();
                }, true)

                .addButton(new ImageIcon(createFlippedImage(getBufferedImage("arrow.png"))), () ->

                {
                    if (!priorityTableElement.isRowSelected()) {
                        showMessage("Select Priority to move down");
                        return;
                    }
                    priorityTableElement.moveDownwards();
                }, true)
                .endRow()
                .addLabel("<html><br></html>")
                .addRow()
                .addCustomElement(priorityTableElement)
                .endRow()
                .build();

        Dialog dialog = new Dialog(new Object[]{"CANCEL"}, form.getForm().getContentPane());
        dialog.showDialog(null, "Priority", getIcon("anch.png"), false);
    }

    private TableElement<Priority> getPriorityTableElement() {
        return new TableElement<>("ALL PRIORITY",
                List.of(setBoldAndUnderlined(NAME_LABEL), setBoldAndUnderlined(WEIGHT_LABEL)),
                List.of("name", "weight"),
                priorityService.getAll(), true);
    }

}
