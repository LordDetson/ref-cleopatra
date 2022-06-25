package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Priority;
import com.jazzteam.cleopatra.repository.PriorityRepository;
import com.jazzteam.cleopatra.repository.impl.EmbeddedPriorityRepository;
import com.jazzteam.cleopatra.service.impl.PriorityServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jazzteam.cleopatra.service.EntityTestFabrics.createAnyPriority;
import static com.jazzteam.cleopatra.service.EntityTestFabrics.createValidPriority;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PriorityServiceTest {

    private final String PRIORITY_NAME_FOR_UPDATE = "new name for priority";

    private PriorityService priorityService = new PriorityServiceImpl();
    private PriorityRepository priorityRepository = new EmbeddedPriorityRepository();

    private Priority priorityFromRepository;

    @BeforeEach
    void setUp() {
        priorityFromRepository = priorityRepository.create(createValidPriority());
    }

    @AfterEach
    void tearDown() {
        if (priorityRepository.get(priorityFromRepository.getId()) != null) {
            priorityRepository.delete(priorityFromRepository.getId());
        }
    }

    @Test
    void create() {
        //Given
        final String newTestName = "NEW_TEST_NAME";
        Priority testPriority = createAnyPriority(null, newTestName, 11);

        //When
        final Priority priorityCreated = priorityService.create(testPriority);
        testPriority.setId(priorityCreated.getId());

        //Then
        assertEquals(priorityRepository.get(priorityCreated.getId()), testPriority);
    }

    @Test
    void createWithExistNameReturnIllegalArgumentException() {
        //Given
        final String newTestName = "NEW_TEST_NAME222";
        Priority testPriority = createAnyPriority(null, newTestName, 11);

        //When
        final Priority priorityCreated = priorityService.create(testPriority);
        testPriority.setId(priorityCreated.getId());

        //Then
        assertEquals(priorityRepository.get(priorityCreated.getId()), testPriority);
        assertThrows(IllegalArgumentException.class, () -> priorityService.create(testPriority));
    }

    @Test
    void getOrThrowTestThrowIfNotExist() {
        //Given
        final String newTestName = "NEW_TEST_NAME";
        Priority testPriority = createAnyPriority(800, newTestName, 11);

        //Then
        assertThrows(IllegalStateException.class, () -> priorityService.getOrThrow(testPriority.getId()));
    }


    @Test
    void update() {
        //Given
        final Priority testPriority = EntityTestFabrics.createAnyPriority(0, PRIORITY_NAME_FOR_UPDATE, 155);
        testPriority.setId(priorityFromRepository.getId());

        //When
        final Priority priorityUpdated = priorityService.update(priorityFromRepository.getId(), testPriority);

        //Then
        assertEquals(testPriority.getName(), priorityUpdated.getName());
        assertEquals(testPriority.getWeight(), priorityUpdated.getWeight());
    }

    @Test
    void get() {
        //When
        Priority priority = priorityService.get(priorityFromRepository.getId());

        //Then
        assertEquals(priority, priorityFromRepository);
    }

    @Test
    void getOrThrowTest() {
        //When
        Priority priority = priorityService.getOrThrow(priorityFromRepository.getId());

        //Then
        assertEquals(priority, priorityFromRepository);
    }

    @Test
    void getAll() {
        //Given
        final List<Priority> allPriorityFromRepo = priorityRepository.getAll();

        //When
        final List<Priority> priorityList = priorityService.getAll();

        //Then
        assertThat(priorityList)
                .hasSize(allPriorityFromRepo.size())
                .containsAll(allPriorityFromRepo);
    }

    @Test
    void delete() {
        //Given
        assertNotNull(priorityRepository.getOrThrow(priorityFromRepository.getId()));

        //When
        priorityService.delete(priorityFromRepository.getId());

        //Then
        assertThat(priorityRepository.get(priorityFromRepository.getId())).isNull();
    }
}