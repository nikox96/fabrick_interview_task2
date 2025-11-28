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
@Schema(description = "Airport information")
public class Airport {
    @Schema(description = "Airport ID (ICAO code)", example = "KDEN")
    private String id;

    @Schema(description = "Airport name", example = "DENVER INTL")
    private String name;

    @Schema(description = "State code", example = "CO")
    private String state;

    @Schema(description = "Country code", example = "US")
    private String country;

    @Schema(description = "Latitude in degrees", example = "39.8617")
    private Double latitude;

    @Schema(description = "Longitude in degrees", example = "-104.6732")
    private Double longitude;

    @Schema(description = "Elevation in meters", example = "1656.6")
    private Double elevation;
}