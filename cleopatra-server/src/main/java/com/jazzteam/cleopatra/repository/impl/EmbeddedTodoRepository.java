package com.jazzteam.cleopatra.repository.impl;

import com.jazzteam.cleopatra.entity.Todo;
import com.jazzteam.cleopatra.repository.TodoRepository;

import java.time.LocalDate;
import java.util.*;

public class EmbeddedTodoRepository implements TodoRepository {
    private static final String NOT_FOUND_MESSAGE = "Todo not found";
    private static final String ALREADY_EXIST = "Todo with such id already exists";

    private static final EmbeddedPriorityRepository priorityRepository = new EmbeddedPriorityRepository();
    private static final EmbeddedStatusRepository statusRepository = new EmbeddedStatusRepository();

    private static final Map<UUID, Todo> todoMap = new HashMap<>();

    static {
        initTodos();
    }

    private static void initTodos() {
        Todo todo1 = Todo.builder().id(UUID.randomUUID())
                .createDate(LocalDate.of(2022, 5, 11))
                .description("To do task from the 1st phase")
                .priority(priorityRepository.get(2))
                .title("Main task ever")
                .status(statusRepository.get(1))
                .build();

        Todo todo2 = Todo.builder().id(UUID.randomUUID())
                .createDate(LocalDate.of(2022, 4, 27))
                .description("Submit diploma")
                .priority(priorityRepository.get(1))
                .title("Long task description for checking representation in entity form and tables from ui on swing. Bla bla\n with moving to new.")
                .status(statusRepository.get(2))
                .build();

        Todo todo3 = Todo.builder().id(UUID.randomUUID())
                .createDate(LocalDate.of(2021, 5, 15))
                .description("Submit diploma")
                .priority(priorityRepository.get(0))
                .title("Not Long task description")
                .status(statusRepository.get(3))
                .build();

        todoMap.put(todo1.getId(), todo1);
        todoMap.put(todo2.getId(), todo2);
        todoMap.put(todo3.getId(), todo3);
    }

    @Override
    public Todo create(Todo todo) {
        if (get(todo.getId()) != null) {
            throw new IllegalStateException(ALREADY_EXIST);
        }
        todoMap.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public Todo update(UUID id, Todo todo) {
        final Todo foundedTodo = getOrThrow(id);

        todo.setId(id);
        todo.setCreateDate(foundedTodo.getCreateDate());

        todoMap.put(todo.getId(), todo);
        return get(id);
    }

    @Override
    public Todo get(UUID id) {
        return todoMap.get(id);
    }

    @Override
    public List<Todo> getAll() {
        return new ArrayList<>(todoMap.values());
    }

    @Override
    public void delete(UUID id) {
        final Todo todoForDelete = getOrThrow(id);
        todoMap.remove(todoForDelete.getId());
    }

    @Override
    public Todo getOrThrow(UUID id) {
        Todo forReturn = todoMap.get(id);
        if (forReturn == null) {
            throw new IllegalStateException(NOT_FOUND_MESSAGE);
        }
        return forReturn;
    }
}
