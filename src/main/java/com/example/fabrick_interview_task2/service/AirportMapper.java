package com.example.fabrick_interview_task2.service;

import com.example.fabrick_interview_task2.model.Airport;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AirportMapper {

    @Mapping(source = "icaoId", target = "id")
    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    @Mapping(source = "elev", target = "elevation")
    Airport toAirport(AirportInfoResponse airportInfoResponse);

    List<Airport> toAirport(List<AirportInfoResponse> airportInfoResponse);
}
