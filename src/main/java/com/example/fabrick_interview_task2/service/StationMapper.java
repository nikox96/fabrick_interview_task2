package com.example.fabrick_interview_task2.service;

import com.example.fabrick_interview_task2.model.Station;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StationMapper {

    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    @Mapping(source = "elev", target = "elevation")
    Station toStation(StationInfoResponse stationInfoResponse);

    List<Station> toStation(List<StationInfoResponse> stationInfoResponses);
}
