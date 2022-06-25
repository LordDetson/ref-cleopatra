package com.jazzteam.cleopatra.service.impl;

import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.repository.impl.EmbeddedStatusRepository;
import com.jazzteam.cleopatra.service.StatusService;

import java.util.List;

public class StatusServiceImpl implements StatusService {
    private static final EmbeddedStatusRepository statusRepository = new EmbeddedStatusRepository();

    @Override
    public Status create(Status status) {
        return statusRepository.create(status);
    }

    @Override
    public Status update(Integer id, Status status) {
        return statusRepository.update(id, status);
    }

    @Override
    public Status get(Integer id) {
        return statusRepository.get(id);
    }

    @Override
    public List<Status> getAll() {
        return statusRepository.getAll();
    }

    @Override
    public void delete(Integer id) {
        statusRepository.delete(id);
    }

    private Status getOrThrow(Integer id) {
        return statusRepository.getOrThrow(id);
    }
}
