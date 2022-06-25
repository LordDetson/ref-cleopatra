package com.jazzteam.cleopatra.component.interfaces;

import com.jazzteam.cleopatra.component.base.Form;

@FunctionalInterface
public interface CloseListener {
    void onClose(Form form);
}
