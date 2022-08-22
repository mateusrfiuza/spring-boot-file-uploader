package com.example.file_uploader.entrypoint.http.handler;

import java.util.Set;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, Set<String> message) {

    public ErrorResponse(final HttpStatus status, final String message) {
        this(status, Set.of(message));
    }

}
