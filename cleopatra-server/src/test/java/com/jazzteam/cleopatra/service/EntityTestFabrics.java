package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.entity.Todo;

import java.time.LocalDate;
import java.util.UUID;

public class EntityTestFabrics {

    static public Priority createValidPriority() {
        return createAnyPriority(12, "Valid name", 100);
    }

    static public Priority createAnyPriority(Integer id, String name, Integer weigth) {
        return Priority.builder()
                .id(id)
                .name(name)
                .weight(weigth)
                .build();
    }

    static public Status createValidStatus() {
        return new Status(1, "In progress");
    }

    static public Todo createValidTodo() {
        return createAnyTodo(UUID.randomUUID(),
                "Main task ever",
                LocalDate.of(2022, 5, 11),
                LocalDate.of(2022, 6, 11),
                "To do task from the 1st phase",
                createValidPriority(),
                createValidStatus());
    }

    static public Todo createAnyTodo(UUID uuid, String title, LocalDate createDay, LocalDate endDate, String description, Priority priority, Status status) {
        return Todo.builder().id(uuid)
                .createDate(createDay)
                .endDate(endDate)
                .description(description)
                .priority(createValidPriority())
                .title(title)
                .status(status)
                .build();
    }
}
