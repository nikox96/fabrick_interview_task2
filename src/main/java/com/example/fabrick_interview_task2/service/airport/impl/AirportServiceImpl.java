package com.example.fabrick_interview_task2.service.airport.impl;

import com.example.fabrick_interview_task2.client.impl.AviationWeatherClientImpl;
import com.example.fabrick_interview_task2.model.BoundingBox;
import com.example.fabrick_interview_task2.model.Station;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import com.example.fabrick_interview_task2.service.StationMapper;
import com.example.fabrick_interview_task2.service.airport.AirportService;
import com.example.fabrick_interview_task2.util.BoundingBoxCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AviationWeatherClientImpl aviationWeatherClient;
    private final StationMapper mapper;

    @Override
    public List<Station> getClosestStations(String airportId, double closestBy) {
        log.info("Finding closest stations for airport: {} with closestBy: {}", airportId, closestBy);

        AirportInfoResponse airportInfo = aviationWeatherClient.getAirportInfo(airportId);

        log.debug("Airport found: {} at lat={}, lon={}", airportId, airportInfo.getLat(), airportInfo.getLon());
        BoundingBox bbox = BoundingBoxCalculator.calculateBBox(airportInfo.getLat(), airportInfo.getLon(), closestBy);

        List<StationInfoResponse> stationsInBox = aviationWeatherClient.getStationsInBoundingBox(
                bbox.getMinLatitude(), bbox.getMinLongitude(), bbox.getMaxLatitude(), bbox.getMaxLongitude()
        );

        if (stationsInBox == null || stationsInBox.isEmpty()) {
            log.info("No stations found in bounding box for airport: {}", airportId);
            return Collections.emptyList();
        }

        log.info("Found {} stations for airport: {}", stationsInBox.size(), airportId);

        return mapper.toStation(stationsInBox);
    }
}