package com.example.fabrick_interview_task2.service.airport;

import com.example.fabrick_interview_task2.client.impl.AviationWeatherClientImpl;
import com.example.fabrick_interview_task2.constant.ApplicationError;
import com.example.fabrick_interview_task2.exception.ResourceNotFoundException;
import com.example.fabrick_interview_task2.model.Station;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import com.example.fabrick_interview_task2.service.StationMapper;
import com.example.fabrick_interview_task2.service.StationMapperImpl;
import com.example.fabrick_interview_task2.service.airport.impl.AirportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    private AviationWeatherClientImpl aviationWeatherClient;

    @InjectMocks
    private AirportServiceImpl airportService;

    @Spy
    private StationMapper stationMapper = new StationMapperImpl();

    private AirportInfoResponse airportInfo;
    private StationInfoResponse station1;
    private StationInfoResponse station2;

    @BeforeEach
    void setUp() {
        airportInfo = new AirportInfoResponse();
        airportInfo.setIcaoId("KDEN");
        airportInfo.setName("DENVER INTL");
        airportInfo.setState("CO");
        airportInfo.setCountry("US");
        airportInfo.setLat(39.8617);
        airportInfo.setLon(-104.6732);
        airportInfo.setElev(1656.6);

        station1 = new StationInfoResponse();
        station1.setId("KAFF");
        station1.setSite("Air Force Academy Arfld");
        station1.setState("CO");
        station1.setCountry("US");
        station1.setLat(38.971);
        station1.setLon(-104.816);
        station1.setElev(2003);

        station2 = new StationInfoResponse();
        station2.setId("KCOS");
        station2.setSite("Colorado Springs");
        station2.setState("CO");
        station2.setCountry("US");
        station2.setLat(38.805);
        station2.setLon(-104.700);
        station2.setElev(1881);
    }

    @Test
    void testGetClosestStations_Success() {
        String airportId = "KDEN";
        double closestBy = 1.0;

        when(aviationWeatherClient.getAirportInfo(airportId)).thenReturn(airportInfo);
        when(aviationWeatherClient.getStationsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(Arrays.asList(station1, station2));

        List<Station> result = airportService.getClosestStations(airportId, closestBy);

        assertNotNull(result);
        assertEquals(2, result.size());

        Station firstStation = result.getFirst();
        assertEquals("KAFF", firstStation.getId());

        verify(aviationWeatherClient, times(1)).getAirportInfo(airportId);
        verify(aviationWeatherClient, times(1)).getStationsInBoundingBox(
                eq(38.8617), eq(-105.6732), eq(40.8617), eq(-103.6732)
        );
    }

    @Test
    void testGetClosestStations_AirportNotFound() {
        String airportId = "XXXX";
        double closestBy = 0.0;

        when(aviationWeatherClient.getAirportInfo(airportId))
                .thenThrow(new ResourceNotFoundException(ApplicationError.AIRPORT_NOT_FOUND, airportId));

        assertThrows(ResourceNotFoundException.class, () -> {
            airportService.getClosestStations(airportId, closestBy);
        });

        verify(aviationWeatherClient, times(1)).getAirportInfo(airportId);
        verify(aviationWeatherClient, never()).getStationsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        );
    }

    @Test
    void testGetClosestStations_NoStationsInBoundingBox() {
        String airportId = "KDEN";
        double closestBy = 0.1;

        when(aviationWeatherClient.getAirportInfo(airportId)).thenReturn(airportInfo);
        when(aviationWeatherClient.getStationsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(Collections.emptyList());

        List<Station> result = airportService.getClosestStations(airportId, closestBy);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(aviationWeatherClient, times(1)).getAirportInfo(airportId);
        verify(aviationWeatherClient, times(1)).getStationsInBoundingBox(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        );
    }
}