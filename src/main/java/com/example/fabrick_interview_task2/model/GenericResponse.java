package com.example.fabrick_interview_task2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GenericResponse<T> extends BaseResponse {
    @Schema(description = "Body")
    private T data;
}
