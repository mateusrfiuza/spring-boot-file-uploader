package com.example.file_uploader.service.exception;

public class InvalidFileException extends RuntimeException {

    public InvalidFileException(final String message) {
        super(message);
    }

}
