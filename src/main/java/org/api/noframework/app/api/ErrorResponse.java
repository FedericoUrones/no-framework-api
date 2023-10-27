package org.api.noframework.app.api;

import lombok.Data;

@Data
public class ErrorResponse {

    int code;
    String message;
}