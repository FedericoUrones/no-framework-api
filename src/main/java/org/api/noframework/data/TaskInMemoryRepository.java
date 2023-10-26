package org.api.noframework.data;

import org.api.noframework.domain.NewTask;
import org.api.noframework.domain.Task;
import org.api.noframework.domain.TaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TaskInMemoryRepository implements TaskRepository {

    // TODO: Usar sqlite
    private static final Map<String, Task> TASKS_STORE = new ConcurrentHashMap<>();

    @Override
    public String create(NewTask newTask) {
        String id = UUID.randomUUID().toString();
        Task task = Task.builder()
                .id(id)
                .description(newTask.getDescription())
                .build();
        TASKS_STORE.put(newTask.getDescription(), task);

        return id;
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(TASKS_STORE.values());
    }
}