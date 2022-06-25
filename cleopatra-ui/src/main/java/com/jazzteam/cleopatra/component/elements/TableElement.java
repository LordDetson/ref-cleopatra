package com.jazzteam.cleopatra.component.elements;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.elements.table.TableLoader;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import com.jazzteam.cleopatra.component.util.SystemUiSettingsUtil;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TableElement<T> extends FormElement {

    private JTable table;
    private TableLoader<T> loader;

    @Getter
    private String[] header;

    public TableElement(String label, List<String> header, List<String> bindEntityField, List<T> data, boolean isEditable) {
        super(label);
        initTable(isEditable);
        initHeader(header);

        SystemUiSettingsUtil.applySettingsForTable(table);
        initTableLoader(table, bindEntityField, this.header);
        load(data);
    }

    @Override
    public JComponent createComponent(FormElementChangeListener changeListener) {

        table.getModel().addTableModelListener(event -> {
            if (changeListener != null) {
                changeListener.onChange(TableElement.this, getValue(), getParentForm());
            }
        });

        return new JScrollPane(table);
    }

    @Override
    public List<T> getValue() {
        return loader.getItems();
    }

    @Override
    public void setValue(Object value) {
        Class<T> type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        if (value instanceof List<?>) {
            List<T> list = (List<T>) ((List) value).stream()
                    .filter(item -> type.isInstance(0))
                    .collect(Collectors.toList());

            loader.load(list);

        } else if (type.isInstance(value)) {
            T singleElement = type.cast(value);
            loader.load(Collections.singletonList(singleElement));

        } else if (value instanceof Boolean) {
            table.setDefaultEditor(Object.class, new DefaultCellEditor(new JCheckBox()));

        } else
            throw new IllegalArgumentException(ErrorMessages.BAD_TABLE_MODEL_ERROR.getMessage());
    }

    @Override
    public void setEnabled(boolean enable) {
        table.setEnabled(enable);
    }

    public void addRow(T entity) {
        loader.addRow(entity);
    }

    public void removeRow() {
        loader.removeRow(table.getSelectedRow());
    }

    public boolean isRowSelected() {
        return table.getSelectedRow() != -1;
    }

    public List<T> listSelectedItem() {
        return loader.listSelectedItem();
    }

    public void moveUpwards() {
        moveRowBy(-1);
    }

    public void moveDownwards() {
        moveRowBy(1);
    }

    private void moveRowBy(int lines) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int[] rows = table.getSelectedRows();
        int destination = rows[0] + lines;
        int rowCount = model.getRowCount();

        if (destination < 0 || destination >= rowCount) {
            return;
        }

        model.moveRow(rows[0], rows[rows.length - 1], destination);
        table.setRowSelectionInterval(rows[0] + lines, rows[rows.length - 1] + lines);
        loader.swap(rows[0], destination);
    }

    private void initTableLoader(JTable table, List<String> bindEntityField, String[] header) {
        loader = new TableLoader<>(table, bindEntityField, header);
    }

    private void initHeader(List<String> header) {
        if (header != null && !header.isEmpty()) {
            this.header = header.toArray(String[]::new);
        }

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(this.header);
        table.setModel(tableModel);
    }

    public void load(List<T> data) {
        loader.load(data);
    }

    private void initTable(boolean isEditable) {
        this.table = new JTable();
        if (!isEditable) {
            this.table.setDefaultEditor(Object.class, null);
        }
        SystemUiSettingsUtil.applySettingsForTable(this.table);
    }
}