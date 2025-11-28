package com.example.fabrick_interview_task2.client.impl;

import com.example.fabrick_interview_task2.client.AviationWeatherClient;
import com.example.fabrick_interview_task2.config.AviationWeatherApiProperties;
import com.example.fabrick_interview_task2.constant.ApplicationError;
import com.example.fabrick_interview_task2.exception.AviationApiException;
import com.example.fabrick_interview_task2.exception.ResourceNotFoundException;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
@RequiredArgsConstructor
public class AviationWeatherClientImpl implements AviationWeatherClient {

    static final String AIRPORT_PATH_SEGMENT = "airport";
    static final String STATIONINFO_PATH_SEGMENT = "stationinfo";
    private final RestClient restClient;
    private final AviationWeatherApiProperties apiProperties;

    @Cacheable(value = AIRPORT_PATH_SEGMENT + "Info", key = "#icaoCode")
    public AirportInfoResponse getAirportInfo(String icaoCode) {
        log.debug("Calling Aviation Weather API for " + AIRPORT_PATH_SEGMENT + ": {}", icaoCode);

        String url = UriComponentsBuilder.fromUriString(apiProperties.getBaseUrl()).pathSegment(AIRPORT_PATH_SEGMENT)
                .queryParam("ids", icaoCode)
                .queryParam("format", "json")
                .buildAndExpand()
                .toUriString();
        try {
            List<AirportInfoResponse> airports = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (airports == null || airports.isEmpty()) {
                log.info("No " + AIRPORT_PATH_SEGMENT + " found for ICAO code: {}", icaoCode);
                throw new ResourceNotFoundException(ApplicationError.NO_AIRPORT_FOUND, icaoCode);
            }
            return airports.getFirst();
        } catch (HttpClientErrorException e) {
            log.error("Aviation API client error for icaoCode {}: {} - {}", icaoCode, e.getStatusCode(), e.getMessage(),
                    e);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AviationApiException(e, HttpStatus.NOT_FOUND, ApplicationError.AIRPORT_NOT_FOUND);
            } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new AviationApiException(e, HttpStatus.TOO_MANY_REQUESTS,
                        ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            } else {
                throw new AviationApiException(e, HttpStatus.BAD_GATEWAY, ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            }
        } catch (HttpServerErrorException e) {
            log.error("Aviation API server error for icaoCode {}: {} - {}", icaoCode, e.getStatusCode(), e.getMessage(),
                    e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error calling Aviation API for icaoCode: {}", icaoCode, e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        }
    }

    @Cacheable(value = "stationInfo", key = "#stationId")
    public StationInfoResponse getStationInfo(String stationId) {
        log.debug("Calling Aviation Weather API for station: {}", stationId);

        String url =
                UriComponentsBuilder.fromUriString(apiProperties.getBaseUrl()).pathSegment(STATIONINFO_PATH_SEGMENT)
                        .queryParam("ids", stationId)
                        .queryParam("format", "json")
                        .buildAndExpand()
                        .toUriString();
        try {
            List<StationInfoResponse> stations = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (stations == null || stations.isEmpty()) {
                log.info("No station found for ID: {}", stationId);
                throw new ResourceNotFoundException(ApplicationError.NO_STATION_FOUND, stationId);
            }

            return stations.getFirst();
        } catch (HttpClientErrorException e) {
            log.error("Aviation API client error for stationId {}: {} - {}", stationId, e.getStatusCode(),
                    e.getMessage(), e);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AviationApiException(e, HttpStatus.NOT_FOUND, ApplicationError.AIRPORT_NOT_FOUND);
            } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new AviationApiException(e, HttpStatus.TOO_MANY_REQUESTS,
                        ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            } else {
                throw new AviationApiException(e, HttpStatus.BAD_GATEWAY, ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            }
        } catch (HttpServerErrorException e) {
            log.error("Aviation API server error for stationId {}: {} - {}", stationId, e.getStatusCode(),
                    e.getMessage(), e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error calling Aviation API for stationId: {}", stationId, e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        }
    }

    @Cacheable(value = "stationsBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<StationInfoResponse> getStationsInBoundingBox(double minLat, double minLon, double maxLat,
                                                              double maxLon) {
        String bbox = String.format(Locale.US, "%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        log.debug(
                "Calling Aviation Weather API for stations in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);
        String url =
                UriComponentsBuilder.fromUriString(apiProperties.getBaseUrl()).pathSegment(STATIONINFO_PATH_SEGMENT)
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .buildAndExpand()
                        .toUriString();
        try {
            List<StationInfoResponse> stations = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            log.debug("Response body: {}", stations);

            if (stations == null) {
                log.warn("Empty response body for stations bounding box query");
                return Collections.emptyList();
            }

            return stations;
        } catch (HttpClientErrorException e) {
            log.error("Aviation API client error for bounding box: {} - {}", e.getStatusCode(), e.getMessage(), e);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AviationApiException(e, HttpStatus.NOT_FOUND, ApplicationError.AIRPORT_NOT_FOUND);
            } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new AviationApiException(e, HttpStatus.TOO_MANY_REQUESTS,
                        ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            } else {
                throw new AviationApiException(e, HttpStatus.BAD_GATEWAY, ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            }
        } catch (HttpServerErrorException e) {
            log.error("Aviation API server error for bounding box: {} - {}", e.getStatusCode(), e.getMessage(), e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error calling Aviation API for bounding box: {}", e.getMessage(), e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        }
    }

    @Cacheable(value = AIRPORT_PATH_SEGMENT + "sBbox", key = "#minLat + '_' + #minLon + '_' + #maxLat + '_' + #maxLon")
    public List<AirportInfoResponse> getAirportsInBoundingBox(double minLat, double minLon, double maxLat,
                                                              double maxLon) {
        String bbox = String.format(Locale.US, "%f,%f,%f,%f", minLat, minLon, maxLat, maxLon);

        log.debug("Calling Aviation Weather API for " + AIRPORT_PATH_SEGMENT +
                        "s in bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);
        String url =
                UriComponentsBuilder.fromUriString(apiProperties.getBaseUrl()).pathSegment(AIRPORT_PATH_SEGMENT)
                        .queryParam("bbox", bbox)
                        .queryParam("format", "json")
                        .buildAndExpand()
                        .toUriString();
        try {
            List<AirportInfoResponse> airports = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            log.debug("Response body: {}", airports);

            if (airports == null) {
                log.warn("Empty response body for " + AIRPORT_PATH_SEGMENT + "s bounding box query");
                return Collections.emptyList();
            }

            return airports;
        } catch (HttpClientErrorException e) {
            log.error("Aviation API client error for bounding box: {} - {}", e.getStatusCode(), e.getMessage(), e);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AviationApiException(e, HttpStatus.NOT_FOUND, ApplicationError.AIRPORT_NOT_FOUND);
            } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new AviationApiException(e, HttpStatus.TOO_MANY_REQUESTS,
                        ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            } else {
                throw new AviationApiException(e, HttpStatus.BAD_GATEWAY, ApplicationError.UNABLE_TO_RETRIEVE_DATA);
            }
        } catch (HttpServerErrorException e) {
            log.error("Aviation API server error for bounding box: {} - {}", e.getStatusCode(), e.getMessage(), e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error calling Aviation API for bounding box: {}", e.getMessage(), e);
            throw new AviationApiException(e, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationError.GENERIC_ERROR);
        }
    }
}