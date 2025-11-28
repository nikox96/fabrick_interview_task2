package com.example.fabrick_interview_task2.exception;

import com.example.fabrick_interview_task2.constant.ApplicationError;
import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {

    private ApplicationError error;

    public ResourceNotFoundException(ApplicationError error, Object... args) {
        super(error.getMessage().formatted(args));
    }
}