package com.jazzteam.cleopatra.component.util;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;
import com.google.common.base.Preconditions;
import com.jazzteam.cleopatra.component.elements.table.editors.DatePickerRenderer;
import com.jazzteam.cleopatra.component.elements.table.editors.JComboBoxEditor;
import com.jazzteam.cleopatra.component.elements.table.editors.JComboBoxRenderer;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;
import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.service.PriorityService;
import com.jazzteam.cleopatra.service.StatusService;
import com.jazzteam.cleopatra.service.impl.PriorityServiceImpl;
import com.jazzteam.cleopatra.service.impl.StatusServiceImpl;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class TableSettingEditors {

    private static final PriorityService priorityService = new PriorityServiceImpl();
    private static final StatusService statusService = new StatusServiceImpl();

    private TableSettingEditors() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static final Map<Class<?>, TableCellEditor> mapClassEditor = Map.of(
            LocalDate.class, new DateTableEditor(),
            Priority.class, new JComboBoxEditor<>(priorityService.getAll().toArray()),
            Status.class, new JComboBoxEditor<>(statusService.getAll().toArray())
    );

    public static final Map<Class<?>, TableCellRenderer> mapClassRenderer = Map.of(
            LocalDate.class, new DatePickerRenderer(),
            Priority.class, new JComboBoxRenderer<>(priorityService.getAll().toArray()),
            Status.class, new JComboBoxRenderer<>(statusService.getAll().toArray())
    );

    public static void acceptColumnRender(List<Class<?>> columnClasses, JTable table) {
        if (table.getRowCount() <= 0) {
            return;
        }
        Preconditions.checkState(columnClasses.size() == table.getColumnCount(), ErrorMessages.INDEX_OUT_COLUMN_ERROR.getMessage());

        Stream.iterate(0, index -> index + 1)
                .limit(columnClasses.size())
                .forEach(index -> {

                        if (mapClassEditor.containsKey(columnClasses.get(index))) {
                            table.getColumnModel().getColumn(index).setCellEditor(mapClassEditor.get(columnClasses.get(index)));
                        }

                        if (mapClassRenderer.containsKey(columnClasses.get(index))) {
                            table.getColumnModel().getColumn(index).setCellRenderer(mapClassRenderer.get(columnClasses.get(index)));
                        }

                });
    }
}
