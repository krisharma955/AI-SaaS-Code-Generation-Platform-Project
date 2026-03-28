package com.K955.AI_SaaS_Code_Generation_Platform.Exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionApi(
        HttpStatus status,
        String message,
        Instant timestamp
) {
}
