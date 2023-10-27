package org.api.noframework.domain;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public String create(NewTask user) {
        return taskRepository.create(user);
    }

    public List<Task> getAll() {
        return taskRepository.getAll();
    }

    public Task getById(String id) {
        return taskRepository.getById(id);
    }

    public Task update(String taskId, Task task) {
        return taskRepository.update(taskId, task);
    }

    public void delete(String taskId) {
        taskRepository.delete(taskId);
    }
}