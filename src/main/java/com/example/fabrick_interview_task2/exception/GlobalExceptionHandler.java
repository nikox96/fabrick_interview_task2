package com.example.fabrick_interview_task2.exception;

import com.example.fabrick_interview_task2.constant.ApplicationError;
import com.example.fabrick_interview_task2.constant.Status;
import com.example.fabrick_interview_task2.model.error.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, ex.getError().getErrorCode());
    }

    @ExceptionHandler(AviationApiException.class)
    public ResponseEntity<ErrorResponse> handleAviationApiException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getError().getMessage(), request, ex.getError().getErrorCode());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        log.error("HTTP client error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.valueOf(ex.getStatusCode().value()), ApplicationError.UNABLE_TO_RETRIEVE_DATA.getMessage(), request, ApplicationError.UNABLE_TO_RETRIEVE_DATA.getErrorCode());
    }

    @ExceptionHandler({ResourceAccessException.class, HttpServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex, WebRequest request) {
        log.error("Resource access error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ApplicationError.UNABLE_TO_RETRIEVE_DATA.getMessage(), request, ApplicationError.UNABLE_TO_RETRIEVE_DATA.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ApplicationError.VALIDATION_ERROR.getMessage(), request, ApplicationError.VALIDATION_ERROR.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR.getMessage(), request, ApplicationError.GENERIC_ERROR.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, WebRequest request, Integer errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(Status.ERROR)
                .message(message)
                .errorCode(errorCode)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}