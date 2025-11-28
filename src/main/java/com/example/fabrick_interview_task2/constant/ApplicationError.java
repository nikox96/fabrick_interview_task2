package com.example.fabrick_interview_task2.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApplicationError {
    //Aviation Errors
    STATION_NOT_FOUND("Station not found", 4001)
    ,AIRPORT_NOT_FOUND("Airport not found", 4002)
    ,NO_AIRPORT_FOUND("No airport found for ICAO code: {}", 4003)
    ,NO_STATION_FOUND("No station found for id: {}", 4004)
    ,UNABLE_TO_RETRIEVE_DATA("Unable to retrieve asteroid data", 4005)
    //Generic Errors
    ,VALIDATION_ERROR("Validation error", 9998)
    ,GENERIC_ERROR("Generic error", 9999);

    private final String message;
    private final Integer errorCode;
}
