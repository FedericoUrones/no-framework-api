package org.api.noframework.app.api.task;

import org.api.noframework.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskResponse taskToTaskResponse(Task task);

    List<TaskResponse> taskToTaskResponse(List<Task> tasks);
}
