package org.api.noframework.app.api;

import com.sun.net.httpserver.Headers;

public record ResponseEntity<T> (T body, Headers headers, StatusCode statusCode){
}