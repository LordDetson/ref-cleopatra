package com.jazzteam.cleopatra.component.elements;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.interfaces.FormElementChangeListener;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class CheckBoxElementList extends FormElement {

    private final Set<String> selected = new LinkedHashSet<>();
    private final boolean hideFilter;
    private JTextField search;
    private JList<JCheckBox> list;
    private List<String> allItems;

    private FormElementChangeListener onChange;

    public CheckBoxElementList(String label, boolean hideFilter, List<String> possibleValues) {
        super(label);
        this.hideFilter = hideFilter;

        allItems = possibleValues;

        initSearchTextField();
        addKeyListener();

        initCheckBoxList();
        addMouseListener();
    }

    @Override
    public JComponent createComponent(FormElementChangeListener onChange) {
        this.onChange = onChange;
        Box verticalBox = Box.createVerticalBox();
        if (!hideFilter)
            verticalBox.add(search);

        verticalBox.add(new JScrollPane(
                list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

        return verticalBox;
    }

    @Override
    public void setEnabled(boolean enable) {
        search.setEnabled(enable);
        list.setEnabled(enable);
    }

    @Override
    public Object getValue() {
        return new ArrayList<>(this.selected);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof List) {
            allItems = (List<String>) value;
            list.setModel(createModel(allItems));

        } else if (value instanceof String[]) {
            allItems = Arrays.asList((String[]) value);
            list.setModel(createModel(allItems));

        } else {
            throw new IllegalArgumentException(ErrorMessages.HAS_TO_BE_LIST_OF_STRING.getMessage());
        }
    }

    public List<String> getVisibleItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < list.getModel().getSize(); i++) {
            final JCheckBox checkBox = list.getModel().getElementAt(i);
            items.add(checkBox.getText());
        }
        return items;
    }

    public void select(String item) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            final JCheckBox checkBox = list.getModel().getElementAt(i);
            if (checkBox.getText().equals(item)) {
                selectCheckbox(i, true);
            }
        }
        updateUiAndListener();
    }

    public void unselect(String item) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            final JCheckBox checkBox = list.getModel().getElementAt(i);
            if (checkBox.getText().equals(item)) {
                selectCheckbox(i, false);
            }
        }
        updateUiAndListener();
    }

    private DefaultListModel<JCheckBox> createModel(List<String> possibleValues) {
        DefaultListModel<JCheckBox> model = new DefaultListModel<>();
        possibleValues.forEach(value -> model.addElement(new JCheckBox(value, selected.contains(value))));
        return model;
    }

    private void toggleCheckbox(int index) {
        JCheckBox checkbox = list.getModel().getElementAt(index);
        checkbox.setSelected(!checkbox.isSelected());

        if (checkbox.isSelected()) {
            selected.add(checkbox.getText());
        } else {
            selected.remove(checkbox.getText());
        }
    }

    private void selectCheckbox(int index, boolean select) {
        JCheckBox checkbox = list.getModel().getElementAt(index);
        checkbox.setSelected(select);
        if (select) {
            selected.add(checkbox.getText());
        } else {
            selected.remove(checkbox.getText());
        }
    }

    private void addMouseListener() {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index != -1) {
                    toggleCheckbox(index);
                    updateUiAndListener();
                }
            }
        });
    }

    private void addKeyListener() {
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyReleasedMethod(e);
            }

            private void keyReleasedMethod(KeyEvent e) {
                super.keyReleased(e);

                final String searchString = search.getText().toLowerCase();

                list.setModel(createModel(allItems.stream()
                        .filter(item -> item.toLowerCase().contains(searchString))
                        .collect(Collectors.toList())));
            }
        });
    }

    private void updateUiAndListener() {
        list.repaint();

        if (onChange != null)
            onChange.onChange(this, selected, getParentForm());
    }

    private ListCellRenderer<JCheckBox> getCellRenderer() {
        return (listItems, checkbox, index, isSelected, cellHasFocus) -> {

            checkbox.setComponentOrientation(list.getComponentOrientation());
            checkbox.setFont(list.getFont());

            checkbox.setBackground(list.getBackground());
            checkbox.setForeground(list.getForeground());

            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);

            checkbox.setEnabled(list.isEnabled());
            checkbox.setText(checkbox.getText());

            return checkbox;
        };
    }

    private void initCheckBoxList() {
        list = new JList<>(createModel(allItems));
        list.setCellRenderer(getCellRenderer());
    }

    private void initSearchTextField() {
        search = new JTextField();
    }
}
