package com.example.fabrick_interview_task2.exception;

import com.example.fabrick_interview_task2.constant.ApplicationError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class AviationApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ApplicationError error;

    public AviationApiException(Throwable cause, HttpStatus httpStatus, ApplicationError error) {
        super(error.getMessage(), cause);
        this.httpStatus = httpStatus;
        this.error = error;
    }
}