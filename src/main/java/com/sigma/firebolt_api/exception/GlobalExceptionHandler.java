package com.sigma.firebolt_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Recurso não encontrado")
                .message(e.getMessage())
                .time(LocalDateTime.now().toString())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionDetails> handleDuplicateEntryException(DuplicateResourceException e) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .status(HttpStatus.CONFLICT.value())
                .title("Recurso duplicado")
                .message(e.getMessage())
                .time(LocalDateTime.now().toString())
                .build(), HttpStatus.CONFLICT);
    }
}
