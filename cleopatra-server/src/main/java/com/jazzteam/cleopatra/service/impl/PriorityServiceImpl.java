package com.jazzteam.cleopatra.service.impl;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.repository.impl.EmbeddedPriorityRepository;
import com.jazzteam.cleopatra.service.PriorityService;
import com.jazzteam.cleopatra.util.ErrorMessages;

import java.util.List;

public class PriorityServiceImpl implements PriorityService {
    private static final EmbeddedPriorityRepository priorityRepository = new EmbeddedPriorityRepository();

    @Override
    public Priority create(Priority priority) {
        return priorityRepository.create(priority);
    }

    @Override
    public Priority update(Integer id, Priority priority) {
        return priorityRepository.update(id, priority);
    }

    @Override
    public Priority get(Integer id) {
        return priorityRepository.get(id);
    }

    @Override
    public List<Priority> getAll() {
        return priorityRepository.getAll();
    }

    @Override
    public void delete(Integer id) {
        priorityRepository.delete(id);
    }

    @Override
    public Priority getOrThrow(Integer id) {
        return priorityRepository.getOrThrow(id);
    }

    @Override
    public Priority getByName(String priorityName) {
        return priorityRepository.getAll()
                .stream()
                .filter(priority -> priority.getName().equals(priorityName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRIORITY_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public Priority getDefault() {
        return get(0);
    }
}
