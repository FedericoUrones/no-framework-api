package org.api.noframework.app.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.api.noframework.app.api.Constants;
import org.api.noframework.app.api.ErrorResponse;
import org.api.noframework.app.api.StatusCode;


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
        ErrorResponse response = new ErrorResponse();
        if (throwable instanceof InvalidRequestException) {
            setErrorResponse((ApplicationException) throwable, response);
            exchange.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), 0);
        } else if (throwable instanceof ResourceNotFoundException) {
            setErrorResponse((ApplicationException) throwable, response);
            exchange.sendResponseHeaders(StatusCode.NOT_FOUND.getCode(), 0);
        } else if (throwable instanceof MethodNotAllowedException) {
            setErrorResponse((ApplicationException) throwable, response);
            exchange.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.getCode(), 0);
        } else {
            response.setCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
            response.setMessage(throwable.getMessage());
            exchange.sendResponseHeaders(StatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);
        }
        return response;
    }

    private void setErrorResponse(ApplicationException exception, ErrorResponse errorResponse) {
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setCode(exception.getCode());
    }
}