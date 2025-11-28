package com.example.fabrick_interview_task2.service.station.impl;

import com.example.fabrick_interview_task2.client.impl.AviationWeatherClientImpl;
import com.example.fabrick_interview_task2.model.Airport;
import com.example.fabrick_interview_task2.model.BoundingBox;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import com.example.fabrick_interview_task2.service.AirportMapper;
import com.example.fabrick_interview_task2.service.station.StationService;
import com.example.fabrick_interview_task2.util.BoundingBoxCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final AviationWeatherClientImpl aviationWeatherClient;
    private final AirportMapper mapper;

    @Override
    public List<Airport> getClosestAirports(String stationId, Double closestBy) {
        log.info("Finding closest airports for station: {} with closestBy: {}", stationId, closestBy);

        StationInfoResponse stationInfo = aviationWeatherClient.getStationInfo(stationId);

        log.debug("Station found: {} at lat={}, lon={}", stationId, stationInfo.getLat(), stationInfo.getLon());

        BoundingBox bbox = BoundingBoxCalculator.calculateBBox(stationInfo.getLat(), stationInfo.getLon(), closestBy);

        List<AirportInfoResponse> airportsInBox = aviationWeatherClient.getAirportsInBoundingBox(
                bbox.getMinLatitude(), bbox.getMinLongitude(), bbox.getMaxLatitude(), bbox.getMaxLongitude()
        );

        if (airportsInBox == null || airportsInBox.isEmpty()) {
            log.info("No airports found in bounding box for station: {}", stationId);
            return Collections.emptyList();
        }

        log.info("Found {} airports for station: {}", airportsInBox.size(), stationId);

        return mapper.toAirport(airportsInBox);
    }
}