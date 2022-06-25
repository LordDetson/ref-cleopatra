package com.jazzteam.cleopatra.repository.impl;


import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.repository.StatusRepository;

import java.util.*;


public class EmbeddedStatusRepository implements StatusRepository {
    private static final String NOT_FOUND_MESSAGE = "Status not found";
    private static final String ALREADY_EXIST_MESSAGE = "Status with such id already exists";

    private static final Map<Integer, Status> statusMap = new HashMap<>();


    static {
        initPriorities();
    }

    private static void initPriorities() {
        Status status1 = new Status(1, "done");
        Status status2 = new Status(2, "in progress");
        Status status3 = new Status(3, "qa");
        statusMap.put(status1.getId(), status1);
        statusMap.put(status2.getId(), status2);
        statusMap.put(status3.getId(), status3);
    }

    @Override
    public Status create(Status status) {
        status.setId(getLastIndex() + 1);
        statusMap.put(status.getId(), status);
        return get(status.getId());
    }

    @Override
    public Status update(Integer id, Status status) {
        getOrThrow(id);
        status.setId(id);
        statusMap.put(id, status);
        return get(id);
    }

    @Override
    public Status get(Integer id) {
        return statusMap.get(id);
    }

    @Override
    public List<Status> getAll() {
        return new ArrayList<>(statusMap.values());
    }

    @Override
    public void delete(Integer id) {
        statusMap.remove(getOrThrow(id).getId());
    }

    @Override
    public Status getOrThrow(Integer id) {
        Status forReturn = statusMap.get(id);
        if (forReturn == null) {
            throw new IllegalStateException(NOT_FOUND_MESSAGE);
        }
        return forReturn;
    }

    private Integer getLastIndex() {
        Integer lastId;
        if (statusMap.size() > 0) {
            lastId = Collections.max(statusMap.entrySet(), Comparator.comparingInt(Map.Entry::getKey)).getKey();
        } else {
            lastId = 0;
        }
        return lastId;
    }
}
