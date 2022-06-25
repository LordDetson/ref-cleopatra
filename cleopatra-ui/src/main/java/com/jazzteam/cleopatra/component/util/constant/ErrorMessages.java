package com.jazzteam.cleopatra.component.util.constant;

public enum ErrorMessages {
    THE_GIVEN_VALUE_HAS_TO_BE_DATE("The given value has to be of type 'Date'"),
    HAS_TO_BE_LIST_OF_STRING("The given value has to be List<String> or String[]"),
    BAD_TABLE_MODEL_ERROR("Model should correspond to type of the table or be boolean value"),
    INIT_STATIC_CLASS_ERROR("Couldn't initialize util class"),
    INSTALL_LOOK_AND_FEEL_ERROR("Cant install Look and Feel"),
    NUMERIC_FORMAT_ERROR("Value is not numeric"),
    HAS_TO_BE_BOOLEAN("The value has to be a boolean"),
    INDEX_OUT_COLUMN_ERROR("index out of bound exception. Amount of columns is less than require");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}