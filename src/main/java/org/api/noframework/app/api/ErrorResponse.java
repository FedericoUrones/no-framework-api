package org.api.noframework.app.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    int code;
    String message;
}