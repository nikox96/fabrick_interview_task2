package com.example.fabrick_interview_task2.controller;

import com.example.fabrick_interview_task2.constant.Status;
import com.example.fabrick_interview_task2.model.GenericResponse;
import com.example.fabrick_interview_task2.model.Station;
import com.example.fabrick_interview_task2.model.error.ErrorResponse;
import com.example.fabrick_interview_task2.service.airport.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/fabrick/v1.0/airports")
@RequiredArgsConstructor
@Tag(name = "Airports", description = "Airport and Observation Stations API")
public class AirportController {

    private static class SuccessfulResponse extends GenericResponse<List<Station>> { }

    private final AirportService airportService;

    @Operation(
            summary = "Retrieve closest observation stations",
            description = "Given an airport ID, gets all the closest observation stations within a bounding box"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stations successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid parameters (e.g.: ICAO code must be 4 letters long or closestBy must be positive)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Airport not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Bad Gateway - Error communicating with Aviation Weather API",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable - unable to access external API",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{airportId}/stations")
    public ResponseEntity<GenericResponse<List<Station>>> getClosestStations(
            @Parameter(description = "ICAO airport code (must be 4 letters long, e.g., KDEN, KARR)", required = true, example = "KDEN")
            @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 letters long")
            @PathVariable String airportId,
            @Parameter(description = "Bounding box modifier in degrees (must be positive, default 0.0)", example = "1.0")
            @PositiveOrZero(message = "closestBy must be a positive value or zero")
            @RequestParam(required = false, defaultValue = "0.0") double closestBy) {

        List<Station> stations = airportService.getClosestStations(airportId, closestBy);

        GenericResponse<List<Station>> response = new GenericResponse<>();
        response.setData(stations);
        response.setErrorCode(0);
        response.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(response);
    }
}
