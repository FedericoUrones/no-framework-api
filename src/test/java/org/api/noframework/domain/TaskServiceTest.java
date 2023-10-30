package org.api.noframework.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    TaskService taskService;
    TaskRepository taskRepository;

    @BeforeEach
    void init(@Mock TaskRepository taskRepository) {
        this.taskService = new TaskService(taskRepository);
        this.taskRepository = taskRepository;
    }

    List<Task> getTaskList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("asdf", "new task 1"));
        tasks.add(new Task("asdfg", "new task 2"));
        return tasks;
    }

    @Test
    void whenCreateTask_thenSucceed() {
        // Given
        when(taskService.create(any())).thenReturn(UUID.randomUUID().toString());
        NewTask newTask = new NewTask("Task 1");

        // Then
        assertDoesNotThrow(() -> taskService.create(newTask));
        String id = taskService.create(newTask);
        assertNotNull(id);
    }

    @Test
    void whenGetAllTasks_thenSucceed() {
        when(taskRepository.getAll()).thenReturn(getTaskList());
        assertDoesNotThrow(() -> taskService.getAll());

        // Given
        List<Task> tasks = taskService.getAll();

        // Then
        assertNotNull(tasks);
        assertEquals(tasks.size(), 2);
    }

    @Test
    void whenDeleteTask_thenSucceed() {
        doNothing().when(taskRepository).delete(any());

        // Given
        String id = "some-id";

        // Then
        assertDoesNotThrow(() -> taskService.delete(id));
    }

    @Test
    void whenUpdateTask_thenSucceed() {
        when(taskRepository.update(any(), any()))
                .thenAnswer(i -> new Task((String) i.getArguments()[0], ((Task)i.getArguments()[1]).getDescription()));

        // Given
        String id = "some-id";
        String descriptionModified = "description-modified";
        Task task = new Task(id, descriptionModified);

        // Then
        assertDoesNotThrow(() -> taskService.update(id, task));
        Task taskModified = taskService.update(id, task);
        assertEquals(descriptionModified, taskModified.getDescription());
        assertEquals(id, taskModified.getId());
    }

}
