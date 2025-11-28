package com.example.fabrick_interview_task2.model.error;

import com.example.fabrick_interview_task2.constant.Status;
import com.example.fabrick_interview_task2.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Error response model returned when an error occurs")
public class ErrorResponse extends BaseResponse {
    @Schema(description = "Request path that caused the error", example = "/api/fabrick/v1.0/airports/KDEN/stations")
    private String path;

    @Builder
    public ErrorResponse(Status status, Integer errorCode, String message,
                         LocalDateTime timestamp, String path) {
        super(status, errorCode, message, timestamp);
        this.path = path;
    }
}