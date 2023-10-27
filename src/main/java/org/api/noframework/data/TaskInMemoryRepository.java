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
        Task task = new Task(id, newTask.getDescription());
        TASKS_STORE.put(id, task);

        return id;
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(TASKS_STORE.values());
    }

    @Override
    public Task getById(String id) {
        return TASKS_STORE.get(id);
    }

    @Override
    public Task update(String taskId, Task task) {
        TASKS_STORE.put(taskId, task);
        return task;
    }

    @Override
    public void delete(String id) {
        TASKS_STORE.remove(id);
    }


}