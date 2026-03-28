package com.K955.AI_SaaS_Code_Generation_Platform.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionApi> handleBadRequestException(BadRequestException exception) {
        ExceptionApi exceptionApi = new ExceptionApi(HttpStatus.BAD_REQUEST, exception.getMessage(), Instant.now());
        log.error(exceptionApi.message());
        return ResponseEntity.status(exceptionApi.status()).body(exceptionApi);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionApi> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ExceptionApi exceptionApi = new
                ExceptionApi(HttpStatus.NOT_FOUND, exception.getResourceName() +" with id "+ exception.getResourceId() +" not found", Instant.now());
        log.error(exceptionApi.message());
        return ResponseEntity.status(exceptionApi.status()).body(exceptionApi);
    }

}
