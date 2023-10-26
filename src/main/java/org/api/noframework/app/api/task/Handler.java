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

public class Handler extends org.api.noframework.app.api.Handler {

    private final TaskService taskService;
    private final static TaskMapper taskMapper = TaskMapper.INSTANCE;

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_PUT = "PUT";

    public Handler(TaskService taskService, ObjectMapper objectMapper,
                   GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.taskService = taskService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response = switch (exchange.getRequestMethod()) {
            case HTTP_POST -> handleResponse(doPost(exchange.getRequestBody()), exchange);
            case HTTP_GET -> // todo: no haría falta el getrequestbody
                    handleResponse(doGet(exchange.getRequestBody()), exchange);
            case HTTP_PUT -> // todo
                    handleResponse(doPost(exchange.getRequestBody()), exchange);
            case HTTP_DELETE -> // todo
                    handleResponse(doPost(exchange.getRequestBody()), exchange);
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

        NewTask task = NewTask.builder()
                .description(request.description())
                .build();

        String taskId = taskService.create(task);

        TaskResponse response = new TaskResponse(taskId, task.getDescription());

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity<List<TaskResponse>> doGet(InputStream is) {
        TaskRequest request = super.readRequest(is, TaskRequest.class);

        List<Task> response = taskService.getAll();


        return new ResponseEntity<>(taskMapper.taskToTaskResponse(response),
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}