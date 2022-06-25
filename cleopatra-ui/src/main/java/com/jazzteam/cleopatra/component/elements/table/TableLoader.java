package com.jazzteam.cleopatra.component.elements.table;

import com.jazzteam.cleopatra.component.util.TableSettingEditors;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TableLoader<T> {

    private final JTable table;
    private List<T> data;
    private Class<T> entity;
    private final DefaultTableModel model;
    private List<String> bindedProperties;

    public TableLoader(JTable table) {
        this(table, null, null);
    }

    public TableLoader(JTable table, List<String> propertiesToBind) {
        this(table, propertiesToBind, null);
    }

    public TableLoader(JTable table, List<String> propertiesToBind, String[] tableColumns) {
        this.table = table;

        if (propertiesToBind != null && !propertiesToBind.isEmpty()) {
            this.bindedProperties = propertiesToBind;
        }

        if (tableColumns == null) {
            this.model = (DefaultTableModel) table.getModel();
        } else {
            this.model = new DefaultTableModel(null, tableColumns);
            table.setModel(this.model);
        }
    }

    public void swap(int i, int j) {
        Collections.swap(data, i, j);
    }

    public void load(List<T> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        this.data = data;
        model.setRowCount(0);
        entity = (Class<T>) data.get(0).getClass();

        List<String> field = bindedProperties == null ? getFieldNames(entity) : bindedProperties;
        data.forEach(value -> model.addRow(getValues(value, field.toArray())));

        TableSettingEditors.acceptColumnRender(Arrays.stream(entity.getDeclaredFields())
                .filter(field1 -> bindedProperties.contains(field1.getName()))
                .map(Field::getType)
                .collect(Collectors.toList()), table);
    }

    public T getSelectedItem() {
        return data.get(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    public T[] getSelectedItems() {
        if (data.isEmpty()) {
            return null;
        }
        int[] rows = table.getSelectedRows();

        T[] item = (T[]) Array.newInstance(entity, rows.length);
        for (int i = 0; i < rows.length; i++) {
            item[i] = data.get(table.convertRowIndexToModel(rows[i]));
        }
        return item;
    }

    public List<T> listSelectedItem() {
        int[] rows = table.getSelectedRows();
        List<T> selectedData = new ArrayList<>();
        for (int r : rows) {
            selectedData.add(data.get(table.convertRowIndexToModel(r)));
        }
        return selectedData;
    }

    public List<T> getItems() {
        return data;
    }

    public void addRow(T t) {
        List<String> field = bindedProperties == null ? getFieldNames(entity) : bindedProperties;
        data.add(t);
        model.addRow(getValues(t, field.toArray()));
    }

    public void removeRow(int index) {
        data.remove(index);
        model.removeRow(index);
    }

    private Object[] getValues(T value, Object[] field) {
        Object[] values = new Object[field.length];

        for (int i = 0; i < field.length; i++) {
            values[i] = getValue(value, String.valueOf(field[i]));
        }

        return values;
    }

    private boolean fieldExists(T t, String fieldName) {
        return Arrays.stream(t.getClass().getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    @SneakyThrows
    private Object getValue(T t, String field) {
        Class<T> bean = (Class<T>) t.getClass();

        if (fieldExists(t, field)) {
            final Field f = bean.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(t);
        } else {
            final Method m = bean.getDeclaredMethod(field);
            m.setAccessible(true);
            return m.invoke(t);
        }
    }

    private List<String> getFieldNames(Class<T> bean) {
        Field[] fs = bean.getDeclaredFields();

        return Arrays.stream(fs)
                .map(Field::getName)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}