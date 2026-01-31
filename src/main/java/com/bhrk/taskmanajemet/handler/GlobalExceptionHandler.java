package com.bhrk.taskmanajemet.handler;

import com.bhrk.taskmanajemet.dto.error.BaseError;
import com.bhrk.taskmanajemet.exceptions.NotFoundException;
import com.bhrk.taskmanajemet.exceptions.ResourceDuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseError> handleResourceNotFound(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        BaseError error = BaseError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<BaseError> handleResourceDuplicate(ResourceDuplicateException ex) {
        HttpStatus status = HttpStatus.CONFLICT;

        BaseError error = BaseError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseError> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        BaseError error = BaseError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(status).body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        BaseError error = BaseError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(error);
    }
}
