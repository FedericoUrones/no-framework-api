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

}