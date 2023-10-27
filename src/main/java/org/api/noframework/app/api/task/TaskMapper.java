package org.api.noframework.app.api.task;

import org.api.noframework.domain.Task;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public TaskResponse taskToTaskResponse(Task task) {

        return task != null? new TaskResponse(task.getId(), task.getDescription()) : null;
    }

    public List<TaskResponse> taskToTaskResponse(List<Task> tasks) {
        return tasks.stream().map(this::taskToTaskResponse).collect(Collectors.toList());
    }
}
