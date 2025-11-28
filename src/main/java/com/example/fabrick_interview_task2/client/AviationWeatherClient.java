package com.example.fabrick_interview_task2.client;

import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;

public interface AviationWeatherClient {
    AirportInfoResponse getAirportInfo(String icaoCode);
}