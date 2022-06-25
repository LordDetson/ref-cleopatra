package com.jazzteam.cleopatra.component.elements.table.editors;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class JComboBoxEditor<E> extends JComboBox<E> implements TableCellEditor {
    private final transient List<CellEditorListener> listeners = new ArrayList<>();
    private transient int originalValue = 0;
    private final transient List<E> objects;

    public JComboBoxEditor(E[] objects) {
        super(objects);
        this.objects = List.of(objects);
    }

    public boolean isCellEditable(EventObject eventObject) {
        return true;
    }

    public Object getCellEditorValue() {
        return String.valueOf(getSelectedItem());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        int valueIndex = -1;
        if (value == null) {
            return null;
        }

        originalValue = getSelectedIndex();
        if (value instanceof Integer) {
            valueIndex = (Integer) value;
        } else {
            valueIndex = !objects.contains((E)value) ? 0 : objects.indexOf((E)value);
        }

        if ((valueIndex <= 0) || (valueIndex >= this.getItemCount())) {
            valueIndex = 0;
        }

        setSelectedIndex(valueIndex);
        setSelectedItem(objects.get(valueIndex));
        table.setRowSelectionInterval(row, row);
        table.setColumnSelectionInterval(column, column);

        return this;
    }

    public void addCellEditorListener(CellEditorListener cellEditorListener) {
        listeners.add(cellEditorListener);
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public void removeCellEditorListener(CellEditorListener cellEditorListener) {
        listeners.remove(cellEditorListener);
    }

    public boolean shouldSelectCell(EventObject eventObject) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();

        return true;
    }

    private void fireEditingCanceled() {
        setSelectedIndex(originalValue);
        setSelectedItem(objects.get(originalValue));

        ChangeEvent event = new ChangeEvent(this);

        for (int i = listeners.size() - 1; i >= 0; --i) {
            (listeners.get(i)).editingCanceled(event);
        }
    }

    private void fireEditingStopped() {
        ChangeEvent changeEvent = new ChangeEvent(this);

        for (int i = listeners.size() - 1; i >= 0; --i) {
            (listeners.get(i)).editingStopped(changeEvent);
        }
    }
}