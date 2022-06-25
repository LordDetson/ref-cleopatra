package com.jazzteam.cleopatra.util;

public enum ErrorMessages {
    PRIORITY_NAME_SHOULD_BE_UNIQ("Priority name should be uniq"),
    PRIORITY_NOT_FOUND_MESSAGE("Priority not found"),
    TODO_NOT_FOUND_MESSAGE("Todo not found"),
    STATUS_NOT_FOUND_MESSAGE("Status not found"),
    ALREADY_EXIST_MESSAGE("Priority with such id already exists");
    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
