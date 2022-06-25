package com.jazzteam.cleopatra.component.elements.table.editors;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EventObject;

@Slf4j
public class DatePickerRenderer extends JTextField implements TableCellRenderer {
    private static final int CLICK_COUNT_TO_EDIT = 1;
    private final boolean isValid = true;

    public DatePickerRenderer() {
        initBorder(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setCellEditorValue(value);
        initBackground(table, isSelected);
        initBorder(hasFocus);
        setScrollOffset(0);
        return this;
    }


    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= CLICK_COUNT_TO_EDIT;
        }
        return true;
    }

    public void setCellEditorValue(Object value) {
        if (value == null) {
            setText(null);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(value.toString());
            setText(DateTimeFormatter.ofPattern("d MMMM yyyy").format(date));
        } catch (DateTimeParseException dateException) {
            log.debug("Enable to parse to date" + value + " " + dateException.getMessage());
            setText(null);
        }
    }

    private void initBackground(JTable table, boolean isSelected) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
    }

    private void initBorder(boolean focus) {
        setBorder(!focus ? UIManager.getBorder("TableHeader.cellBorder") :  UIManager.getBorder("Table.focusCellHighlightBorder"));
    }
}