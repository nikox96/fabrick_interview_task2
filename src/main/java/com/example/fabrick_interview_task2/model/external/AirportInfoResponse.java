package com.example.fabrick_interview_task2.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirportInfoResponse {
    private String icaoId;
    private String iataId;
    private String name;
    private String state;
    private String country;
    private Double lat;
    private Double lon;
    private Double elev;
}