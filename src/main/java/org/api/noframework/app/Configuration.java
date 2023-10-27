package org.api.noframework.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.noframework.app.errors.GlobalExceptionHandler;
import org.api.noframework.infrastructure.TaskDBRepository;
import org.api.noframework.infrastructure.TaskInMemoryRepository;
import org.api.noframework.domain.TaskService;

public class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TaskInMemoryRepository TASK_REPOSITORY = new TaskInMemoryRepository();

    private static final TaskDBRepository TASK_DB_REPOSITORY = TaskDBRepository.getInstance();
    private static final TaskService TASK_SERVICE = new TaskService(TASK_DB_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static TaskService getTaskService() {
        return TASK_SERVICE;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}