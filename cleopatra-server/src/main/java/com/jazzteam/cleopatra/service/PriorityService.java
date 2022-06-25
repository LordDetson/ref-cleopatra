package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.repository.CrudRepository;

public interface PriorityService extends CrudRepository<Integer, Priority> {
    Priority getByName(String priorityName);

    Priority getDefault();
}
