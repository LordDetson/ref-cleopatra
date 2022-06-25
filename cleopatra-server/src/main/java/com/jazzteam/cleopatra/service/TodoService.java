package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Todo;

import java.util.List;
import java.util.UUID;

public interface TodoService extends CrudService<UUID, Todo> {
    List<Todo> getByPriorityName(String priorityName);

    List<Todo> getByPriority(Integer priorityId);

    Todo getFirstByPriority(Integer id);
}
