package com.jazzteam.cleopatra.component.base;

import com.jazzteam.cleopatra.component.interfaces.CloseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class Dialog {

    private JDialog dialog;
    private final JOptionPane optionPane;
    private final Container[] components;
    private Consumer<JDialog> onDialogCreated;
    private CloseListener formCloseListener;

    public Dialog(Object[] options, Container... components) {
        optionPane = new JOptionPane();
        if (options != null) {
            optionPane.setOptions(options);
        } else {
            optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        }
        this.components = components;
    }

    public void setDialogCreatedListener(Consumer<JDialog> onDialogCreated) {
        this.onDialogCreated = onDialogCreated;
    }

    public void setDialogCreatedListener(CloseListener formCloseListener) {
        this.formCloseListener = formCloseListener;
    }

    public void showDialog(String message, String title, ImageIcon image, boolean resizable) {
        if (message != null && !message.isEmpty())
            optionPane.setMessage(new Object[]{message, components});
        else
            optionPane.setMessage(new Object[]{components});

        dialog = createDialog(title, optionPane);
        if (onDialogCreated != null)
            onDialogCreated.accept(dialog);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (image != null) {
            dialog.setIconImage(image.getImage());
        }


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
            }
        });

        dialog.setResizable(resizable);
        dialog.pack();
        dialog.setVisible(true);
        dialog.dispose();
    }

    private JDialog createDialog(String title, JOptionPane optionPane) {
        return optionPane.createDialog(null, title);
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void dispose() {
        dialog.dispose();
    }

    public Object getOptionPane() {
        return optionPane.getValue();
    }
}
