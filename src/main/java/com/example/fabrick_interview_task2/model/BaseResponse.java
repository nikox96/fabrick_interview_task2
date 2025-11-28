package com.example.fabrick_interview_task2.model;

import com.example.fabrick_interview_task2.constant.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseResponse {
    @Schema(description = "Status", example = "SUCCES")
    private Status status;
    @Schema(description = "Error code", example = "1001")
    private Integer errorCode;
    @Schema(description = "Message that describe the error", example = "2024-01-15T10:30:45")
    private String message;
    @Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
