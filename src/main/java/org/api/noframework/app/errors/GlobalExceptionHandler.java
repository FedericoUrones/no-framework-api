package org.api.noframework.app.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.api.noframework.app.api.Constants;
import org.api.noframework.app.api.ErrorResponse;


import java.io.IOException;
import java.io.OutputStream;

public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();
            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            ErrorResponse response = getErrorResponse(throwable, exchange);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(objectMapper.writeValueAsBytes(response));
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ErrorResponse getErrorResponse(Throwable throwable, HttpExchange exchange) throws IOException {
        ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
        if (throwable instanceof InvalidRequestException) {
            InvalidRequestException exc = (InvalidRequestException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(400, 0);
        } else if (throwable instanceof ResourceNotFoundException) {
            ResourceNotFoundException exc = (ResourceNotFoundException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(404, 0);
        } else if (throwable instanceof MethodNotAllowedException) {
            MethodNotAllowedException exc = (MethodNotAllowedException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(405, 0);
        } else {
            responseBuilder.code(500).message(throwable.getMessage());
            exchange.sendResponseHeaders(500, 0);
        }
        return responseBuilder.build();
    }
}