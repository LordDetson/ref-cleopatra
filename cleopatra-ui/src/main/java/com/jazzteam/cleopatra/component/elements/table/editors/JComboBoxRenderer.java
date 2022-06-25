package com.jazzteam.cleopatra.component.elements.table.editors;

import com.jazzteam.cleopatra.component.util.SystemUiSettingsUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;


public class JComboBoxRenderer<E> extends JComboBox<E> implements TableCellRenderer {
    private final transient List<E> objects;

    public JComboBoxRenderer(E[] objects) {
        super(objects);
        this.objects = List.of(objects);
        initBorderFocusedCell();
    }

    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected);

        if (value == null) {
            return null;
        }

        int valueIndex;

        if (value instanceof Integer) {
            valueIndex = (Integer) value;
        } else {
            valueIndex = objects.indexOf((E) value);
        }

        if ((valueIndex <= 0) || (valueIndex >= this.getItemCount())) {
            valueIndex = 0;
        }

        setSelectedIndex(valueIndex);
        setSelectedItem(objects.get(valueIndex));
        return this;
    }

    private void setBackground(boolean isSelected) {
        if (isSelected){
            setBackground(SystemUiSettingsUtil.Colors.DEFAULT_ORANGE.getColor());
            setForeground(SystemUiSettingsUtil.Colors.DEFAULT_FOREGROUND.getColor());
        }else {
            setBackground(null);
            setForeground(SystemUiSettingsUtil.Colors.DEFAULT_TEXT.getColor());
        }
    }

    private Border initBorderFocusedCell() {
        JLabel exampleDefaultRenderer = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(new JTable(), "", true, true, 0, 0);
        Border borderFocusedCell;
        Border borderUnfocusedCell;
        borderFocusedCell = exampleDefaultRenderer.getBorder();
        borderUnfocusedCell = new EmptyBorder(1, 1, 1, 1);
        setBorder(borderUnfocusedCell);
        setBorder(null);
        return borderFocusedCell;
    }
}