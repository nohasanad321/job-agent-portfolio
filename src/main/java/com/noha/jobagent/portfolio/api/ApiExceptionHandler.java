package com.noha.jobagent.portfolio.api;

import com.noha.jobagent.portfolio.api.dto.ApiErrorResponse;
import com.noha.jobagent.portfolio.service.DemoJobNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DemoJobNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(DemoJobNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse("DEMO_JOB_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidParameters(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse("INVALID_REQUEST", "One or more request parameters are invalid."));
    }
}
