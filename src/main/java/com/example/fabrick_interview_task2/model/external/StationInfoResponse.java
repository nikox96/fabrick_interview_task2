package com.example.fabrick_interview_task2.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationInfoResponse {
    private String id;
    private String site;
    private String state;
    private String country;
    private Double lat;
    private Double lon;
    private Integer elev;
}