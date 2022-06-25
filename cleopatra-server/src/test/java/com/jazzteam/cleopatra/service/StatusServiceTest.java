package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Status;
import com.jazzteam.cleopatra.repository.StatusRepository;
import com.jazzteam.cleopatra.repository.impl.EmbeddedStatusRepository;
import com.jazzteam.cleopatra.service.impl.StatusServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jazzteam.cleopatra.service.EntityTestFabrics.createValidStatus;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusServiceTest {
    private final String STATUS_NAME_FOR_UPDATE = "new name for status";

    private StatusService statusService = new StatusServiceImpl();
    private StatusRepository statusRepository = new EmbeddedStatusRepository();

    private Status statusFromRepository;

    @BeforeEach
    void setUp() {
        statusFromRepository = statusRepository.create(createValidStatus());
    }

    @AfterEach
    void tearDown() {
        statusService = new StatusServiceImpl();
    }

    @Test
    void create() {
        //Given
        Status testStatus = createValidStatus();

        //When
        final Status statusCreated = statusService.create(testStatus);
        testStatus.setId(statusCreated.getId());

        //Then
        assertEquals(statusRepository.get(statusCreated.getId()), testStatus);
    }

    @Test
    void update() {
        //Given
        Status testStatusForCreate = createValidStatus();

        final Status testStatus = new Status(1, STATUS_NAME_FOR_UPDATE);
        statusFromRepository.setId(testStatus.getId());

        //When
        final Status statusUpdated = statusService.update(statusFromRepository.getId(), testStatus);

        //Then
        assertEquals(testStatus.getName(), statusUpdated.getName());
    }

    @Test
    void get() {
        //When
        Status status = statusService.get(statusFromRepository.getId());

        //Then
        assertEquals(status, statusFromRepository);
    }

    @Test
    void getAll() {
        //Given
        final List<Status> allStatusFromRepo = statusRepository.getAll();

        //When
        final List<Status> statusList = statusService.getAll();

        //Then
        assertThat(statusList)
                .hasSize(allStatusFromRepo.size())
                .containsAll(allStatusFromRepo);
    }

    @Test
    void delete() {
        //When
        statusService.delete(statusFromRepository.getId());

        //Then
        assertThat(statusRepository.get(statusFromRepository.getId())).isNull();
    }
}
