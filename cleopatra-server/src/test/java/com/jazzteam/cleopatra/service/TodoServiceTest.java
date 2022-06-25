package com.jazzteam.cleopatra.service;

import com.jazzteam.cleopatra.entity.Todo;
import com.jazzteam.cleopatra.repository.TodoRepository;
import com.jazzteam.cleopatra.repository.impl.EmbeddedTodoRepository;
import com.jazzteam.cleopatra.service.impl.TodoServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.jazzteam.cleopatra.service.EntityTestFabrics.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoServiceTest {
    private static final LocalDate TEST_LOCAL_DATE = LocalDate.of(2022, 2, 2);
    private static final String TEST_TITLE = "TITLE";
    private static final String TEST_DESCRIPTION = "TEST DESCRIPTION FOR TEST SUIT";

    TodoService todoService = new TodoServiceImpl();
    TodoRepository todoRepository = new EmbeddedTodoRepository();

    Todo todoFromRepository;

    @BeforeEach
    void setUp() {
        todoFromRepository = todoRepository.create(createValidTodo());
    }

    @AfterEach
    void tearDown() {
        todoService = new TodoServiceImpl();
    }

    @Test
    void create() {
        //Given
        Todo testTodo = createValidTodo();

        //When
        final Todo todoCreated = todoService.create(testTodo);
        testTodo.setId(todoCreated.getId());

        //Then
        assertEquals(todoRepository.get(todoCreated.getId()), testTodo);
    }

    @Test
    void update() {
        //Given
        final Todo testTodo = createAnyTodo(UUID.randomUUID(),
                TEST_TITLE,
                TEST_LOCAL_DATE,
                TEST_LOCAL_DATE.plusDays(10),
                TEST_DESCRIPTION,
                createValidPriority(),
                createValidStatus());

        //When
        final Todo todoUpdated = todoService.update(todoFromRepository.getId(), testTodo);

        //Then
        assertEquals(testTodo.getCreateDate(), todoUpdated.getCreateDate());
        assertEquals(testTodo.getDescription(), todoUpdated.getDescription());
        assertEquals(testTodo.getTitle(), todoUpdated.getTitle());
        assertEquals(testTodo.getPriority(), todoUpdated.getPriority());
        assertEquals(testTodo.getStatus(), todoUpdated.getStatus());
        assertEquals(testTodo.getEndDate(), todoUpdated.getEndDate());
    }

    @Test
    void get() {
        //When
        Todo todo = todoService.get(todoFromRepository.getId());

        //Then
        assertEquals(todo, todoFromRepository);
    }

    @Test
    void getAll() {
        //Given
        final List<Todo> allTodoFromRepo = todoRepository.getAll();

        //When
        final List<Todo> todoList = todoService.getAll();

        //Then
        assertThat(todoList)
                .hasSize(allTodoFromRepo.size())
                .containsAll(allTodoFromRepo);
    }

    @Test
    void delete() {
        //When
        todoService.delete(todoFromRepository.getId());

        //Then
        AssertionsForClassTypes.assertThat(todoRepository.get(todoFromRepository.getId())).isNull();
    }
}
