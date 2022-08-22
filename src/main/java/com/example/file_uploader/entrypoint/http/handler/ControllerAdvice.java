package com.example.file_uploader.entrypoint.http.handler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import com.example.file_uploader.service.exception.InvalidFileException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {InvalidFileException.class, MultipartException.class})
    protected ResponseEntity<ErrorResponse> handleInvalidInputData(final Exception ex) {
        final var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException ex) {
        final var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, buildConstrainViolationMessage(ex));
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class, SizeLimitExceededException.class})
    protected ResponseEntity<ErrorResponse> handleExceedUploadSize(final Exception ex) {
        final var errorResponse = new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, Set.of("Upload file size exceeded"));
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleGeneralException(final Exception ex) {
        final var errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }

    private Set<String> buildConstrainViolationMessage(final ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                 .stream()
                 .map(ConstraintViolation::getMessage)
                 .collect(
                         Collectors.toSet());

    }

}
