package com.jazzteam.cleopatra.repository.impl;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.repository.PriorityRepository;
import com.jazzteam.cleopatra.util.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class EmbeddedPriorityRepository implements PriorityRepository {

    private static final Map<Integer, Priority> priorityMap = new HashMap<>();

    static {
        initPriorities();
    }

    private static void initPriorities() {
        Priority priority = Priority.builder()
                .id(0)
                .name("default")
                .weight(0)
                .build();

        Priority priority1 = Priority.builder()
                .id(1)
                .name("high")
                .weight(10)
                .build();

        Priority priority2 = Priority
                .builder()
                .id(2)
                .name("middle")
                .weight(5)
                .build();

        Priority priority3 = Priority
                .builder()
                .id(3)
                .name("low")
                .weight(3)
                .build();

        priorityMap.put(priority.getId(), priority);
        priorityMap.put(priority1.getId(), priority1);
        priorityMap.put(priority2.getId(), priority2);
        priorityMap.put(priority3.getId(), priority3);
    }

    @Override
    public Priority create(Priority priority) {
        if (priorityMap.entrySet().stream().anyMatch(integerPriorityEntry -> integerPriorityEntry.getValue().getName().equals(priority.getName()))) {
            log.debug("{} -> {}", this.getClass(), ErrorMessages.PRIORITY_NAME_SHOULD_BE_UNIQ.getMessage());
            throw new IllegalArgumentException("Priority name" + priority.getName() + " -> " + ErrorMessages.PRIORITY_NAME_SHOULD_BE_UNIQ.getMessage());
        }
        priority.setId(getLastIndex() + 1);
        priorityMap.put(priority.getId(), priority);
        return priority;
    }


    @Override
    public Priority update(Integer id, Priority priority) {
        getOrThrow(id);
        priority.setId(id);

        priorityMap.put(priority.getId(), priority);
        return get(id);
    }

    @Override
    public Priority get(Integer id) {
        return priorityMap.get(id);
    }

    @Override
    public List<Priority> getAll() {
        return new ArrayList<>(priorityMap.values());
    }

    @Override
    public void delete(Integer id) {
        priorityMap.remove(getOrThrow(id).getId());
    }

    @Override
    public Priority getOrThrow(Integer id) {
        Priority forReturn = priorityMap.get(id);
        if (forReturn == null) {
            log.debug("{} -> {}", this.getClass(), ErrorMessages.PRIORITY_NOT_FOUND_MESSAGE.getMessage());
            throw new IllegalStateException("Priority by id = " + id + " not exist" + ErrorMessages.PRIORITY_NOT_FOUND_MESSAGE.getMessage());
        }
        return forReturn;
    }

    private Integer getLastIndex() {
        Integer lastId;
        if (priorityMap.size() > 0) {
            lastId = Collections
                    .max(priorityMap.entrySet(), Comparator.comparingInt(Map.Entry::getKey))
                    .getKey();

        } else {
            lastId = 0;
        }
        return lastId;
    }
}
