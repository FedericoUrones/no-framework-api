package org.api.noframework.domain;

import java.util.List;

public interface TaskRepository {

    String create(NewTask task);

    List<Task> getAll();

    Task getById(String id);

    Task update(String taskId, Task task);

    void delete(String id);

}