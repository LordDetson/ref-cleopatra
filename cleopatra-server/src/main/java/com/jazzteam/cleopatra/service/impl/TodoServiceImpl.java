package com.jazzteam.cleopatra.service.impl;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.entity.Todo;
import com.jazzteam.cleopatra.repository.impl.EmbeddedTodoRepository;
import com.jazzteam.cleopatra.service.PriorityService;
import com.jazzteam.cleopatra.service.TodoService;
import com.jazzteam.cleopatra.util.ErrorMessages;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoServiceImpl implements TodoService {

    private static final PriorityService priorityService = new PriorityServiceImpl();
    private static final EmbeddedTodoRepository todoRepository = new EmbeddedTodoRepository();


    @Override
    public Todo create(Todo todo) {
        todo.setCreateDate(LocalDate.now());
        todo.setId(UUID.randomUUID());
        return todoRepository.create(todo);
    }

    @Override
    public Todo update(UUID id, Todo todo) {
        return todoRepository.update(id, todo);
    }

    @Override
    public Todo get(UUID id) {
        return todoRepository.get(id);
    }

    @Override
    public List<Todo> getAll() {
        return todoRepository.getAll();
    }

    @Override
    public List<Todo> getByPriorityName(String priorityName) {
        final Priority priorityByName = priorityService.getByName(priorityName);

        return todoRepository.getAll().stream()
                .filter(todo -> Objects.equals(todo.getPriority(), priorityByName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Todo> getByPriority(Integer priorityId) {
        final Priority priorityByName = priorityService.getOrThrow(priorityId);

        return todoRepository.getAll().stream()
                .filter(todo -> Objects.equals(todo.getPriority(), priorityByName))
                .collect(Collectors.toList());
    }

    @Override
    public Todo getFirstByPriority(Integer priorityId) {
        return getByPriority(priorityId).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.TODO_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public void delete(UUID id) {
        todoRepository.delete(id);
    }
}
