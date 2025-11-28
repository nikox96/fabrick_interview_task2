package com.example.fabrick_interview_task2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Observation station information")
public class Station {
    @Schema(description = "Station ID (ICAO code)", example = "KAFF")
    private String id;

    @Schema(description = "Station site name", example = "Air Force Academy Arfld")
    private String site;

    @Schema(description = "State code", example = "CO")
    private String state;

    @Schema(description = "Country code", example = "US")
    private String country;

    @Schema(description = "Latitude in degrees", example = "38.971")
    private Double latitude;

    @Schema(description = "Longitude in degrees", example = "-104.816")
    private Double longitude;

    @Schema(description = "Elevation in meters", example = "2003")
    private Integer elevation;
}