package org.api.noframework.app.api.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.api.noframework.app.api.Constants;
import org.api.noframework.app.api.ResponseEntity;
import org.api.noframework.app.api.StatusCode;
import org.api.noframework.app.errors.ApplicationExceptions;
import org.api.noframework.app.errors.GlobalExceptionHandler;
import org.api.noframework.domain.NewTask;
import org.api.noframework.domain.Task;
import org.api.noframework.domain.TaskService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.api.noframework.app.api.ApiUtils.getPathParam;

public class Handler extends org.api.noframework.app.api.Handler {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_PUT = "PUT";

    public Handler(TaskService taskService, ObjectMapper objectMapper,
                   GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.taskService = taskService;
        this.taskMapper = new TaskMapper();
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response = switch (exchange.getRequestMethod()) {
            case HTTP_POST -> handleResponse(doPost(exchange.getRequestBody()), exchange);
            case HTTP_GET ->
                    handleResponse(doGet(getPathParam(exchange.getRequestURI().getRawPath())), exchange);
            case HTTP_PUT ->
                    handleResponse(doPut(getPathParam(exchange.getRequestURI().getRawPath()), exchange.getRequestBody()), exchange);
            case HTTP_DELETE ->
                    handleResponse(doDelete(getPathParam(exchange.getRequestURI().getRawPath())), exchange);
            default -> throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        };

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private byte[] handleResponse(ResponseEntity response, HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().putAll(response.headers());
        exchange.sendResponseHeaders(response.statusCode().getCode(), 0);
        return super.writeResponse(response.body());
    }

    private ResponseEntity<TaskResponse> doPost(InputStream is) {
        TaskRequest request = super.readRequest(is, TaskRequest.class);
        NewTask task = new NewTask(request.description());
        String taskId = taskService.create(task);

        return new ResponseEntity<>(new TaskResponse(taskId, task.getDescription()),
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity doGet(String id) {

        if (id != null && !id.isEmpty()) {
            Task result = taskService.getById(id);
            return new ResponseEntity<>(taskMapper.taskToTaskResponse(result),
                    getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        } else {
            List<Task> result = taskService.getAll();
            return new ResponseEntity<>(taskMapper.taskToTaskResponse(result),
                    getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }
    }

    private ResponseEntity<TaskResponse> doPut(String taskId, InputStream is) {
        TaskRequest request = super.readRequest(is, TaskRequest.class);
        Task modifiedTask = taskService.update(taskId, new Task(request.id(), request.description()));

        return new ResponseEntity<>(taskMapper.taskToTaskResponse(modifiedTask),
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity<TaskResponse> doDelete(String taskId) {
        taskService.delete(taskId);

        return new ResponseEntity<>(null,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}