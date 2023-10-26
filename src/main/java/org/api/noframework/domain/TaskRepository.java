package org.api.noframework.domain;

import java.util.List;

public interface TaskRepository {

    String create(NewTask task);

    List<Task> getAll();

}