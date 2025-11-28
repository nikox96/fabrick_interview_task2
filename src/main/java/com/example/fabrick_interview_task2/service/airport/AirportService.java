package com.example.fabrick_interview_task2.service.airport;

import com.example.fabrick_interview_task2.model.Station;

import java.util.List;


public interface AirportService {

    List<Station> getClosestStations(String airportId, double closestBy);

}