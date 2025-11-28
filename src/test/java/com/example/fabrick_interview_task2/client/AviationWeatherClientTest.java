package com.example.fabrick_interview_task2.client;

import com.example.fabrick_interview_task2.client.impl.AviationWeatherClientImpl;
import com.example.fabrick_interview_task2.config.AviationWeatherApiProperties;
import com.example.fabrick_interview_task2.constant.ApplicationError;
import com.example.fabrick_interview_task2.exception.AviationApiException;
import com.example.fabrick_interview_task2.exception.ResourceNotFoundException;
import com.example.fabrick_interview_task2.model.external.AirportInfoResponse;
import com.example.fabrick_interview_task2.model.external.StationInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AviationWeatherClient Tests")
class AviationWeatherClientTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private AviationWeatherClientImpl aviationWeatherClient;

    @BeforeEach
    void setUp() {
        AviationWeatherApiProperties apiProperties = new AviationWeatherApiProperties();
        apiProperties.setBaseUrl("http://localhost:8080");
        aviationWeatherClient = new AviationWeatherClientImpl(restClient, apiProperties);
    }

    @Nested
    @DisplayName("getAirportInfo() Tests")
    class GetAirportInfoTests {

        @Test
        @DisplayName("Should return airport info when API returns valid data")
        void shouldReturnAirportInfoWhenApiReturnsValidData() {
            String icaoCode = "KDEN";
            AirportInfoResponse expectedAirport = createAirportInfo("KDEN", "DEN", "Denver International Airport");
            List<AirportInfoResponse> responseList = Collections.singletonList(expectedAirport);

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(responseList);

            AirportInfoResponse result = aviationWeatherClient.getAirportInfo(icaoCode);

            assertThat(result).isNotNull();
            assertThat(result.getIcaoId()).isEqualTo("KDEN");
            assertThat(result.getIataId()).isEqualTo("DEN");
            assertThat(result.getName()).isEqualTo("Denver International Airport");

            verify(restClient, times(1)).get();
            verify(requestHeadersUriSpec, times(1)).retrieve();
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when API returns empty list")
        void shouldThrowResourceNotFoundExceptionWhenApiReturnsEmptyList() {
            String icaoCode = "KXYZ";
            List<AirportInfoResponse> emptyList = Collections.emptyList();

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(emptyList);

            assertThatThrownBy(() -> aviationWeatherClient.getAirportInfo(icaoCode))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(ApplicationError.NO_AIRPORT_FOUND.getMessage().formatted(icaoCode));
        }

    }

    @Nested
    @DisplayName("getStationInfo() Tests")
    class GetStationInfoTests {

        @Test
        @DisplayName("Should return station info when API returns valid data")
        void shouldReturnStationInfoWhenApiReturnsValidData() {
            String stationId = "KAFF";
            StationInfoResponse expectedStation = createStationInfo("KAFF", "US Air Force Academy Airfield");
            List<StationInfoResponse> responseList = Collections.singletonList(expectedStation);

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(responseList);

            StationInfoResponse result = aviationWeatherClient.getStationInfo(stationId);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("KAFF");
            assertThat(result.getSite()).isEqualTo("US Air Force Academy Airfield");

            verify(restClient, times(1)).get();
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when API returns empty list")
        void shouldThrowResourceNotFoundExceptionWhenApiReturnsEmptyList() {
            String stationId = "KXYZ";
            List<StationInfoResponse> emptyList = Collections.emptyList();

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(emptyList);

            assertThatThrownBy(() -> aviationWeatherClient.getStationInfo(stationId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(ApplicationError.NO_STATION_FOUND.getMessage().formatted(stationId));
        }
        
    }

    @Nested
    @DisplayName("getStationsInBoundingBox() Tests")
    class GetStationsInBoundingBoxTests {

        @Test
        @DisplayName("Should return list of stations when API returns valid data")
        void shouldReturnListOfStationsWhenApiReturnsValidData() {
            double minLat = 38.0;
            double minLon = -105.0;
            double maxLat = 40.0;
            double maxLon = -104.0;

            List<StationInfoResponse> expectedStations = Arrays.asList(
                    createStationInfo("KAFF", "US Air Force Academy Airfield"),
                    createStationInfo("KCOS", "Colorado Springs Airport")
            );

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expectedStations);

            List<StationInfoResponse> result = aviationWeatherClient.getStationsInBoundingBox(minLat, minLon, maxLat, maxLon);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo("KAFF");
            assertThat(result.get(1).getId()).isEqualTo("KCOS");
        }


        @Test
        @DisplayName("Should propagate exceptions from RestClient")
        void shouldPropagateExceptionsFromRestClient() {
            double minLat = 38.0;
            double minLon = -105.0;
            double maxLat = 40.0;
            double maxLon = -104.0;

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class)))
                    .thenThrow(new ResourceAccessException("Network error"));

            assertThatThrownBy(() -> aviationWeatherClient.getStationsInBoundingBox(minLat, minLon, maxLat, maxLon))
                    .isInstanceOf(AviationApiException.class)
                    .hasMessageContaining(ApplicationError.GENERIC_ERROR.getMessage());
        }
    }

    @Nested
    @DisplayName("getAirportsInBoundingBox() Tests")
    class GetAirportsInBoundingBoxTests {

        @Test
        @DisplayName("Should return list of airports when API returns valid data")
        void shouldReturnListOfAirportsWhenApiReturnsValidData() {
            double minLat = 39.0;
            double minLon = -105.0;
            double maxLat = 40.0;
            double maxLon = -104.0;

            List<AirportInfoResponse> expectedAirports = Arrays.asList(
                    createAirportInfo("KDEN", "DEN", "Denver International Airport"),
                    createAirportInfo("KBJC", "BJC", "Rocky Mountain Metropolitan Airport")
            );

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expectedAirports);

            List<AirportInfoResponse> result = aviationWeatherClient.getAirportsInBoundingBox(minLat, minLon, maxLat, maxLon);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getIcaoId()).isEqualTo("KDEN");
            assertThat(result.get(1).getIcaoId()).isEqualTo("KBJC");
        }

        @Test
        @DisplayName("Should propagate HTTP errors from RestClient")
        void shouldPropagateHttpErrorsFromRestClient() {
            double minLat = 39.0;
            double minLon = -105.0;
            double maxLat = 40.0;
            double maxLon = -104.0;

            when(restClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(any(ParameterizedTypeReference.class)))
                    .thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));

            assertThatThrownBy(() -> aviationWeatherClient.getAirportsInBoundingBox(minLat, minLon, maxLat, maxLon))
                    .isInstanceOf(AviationApiException.class);
        }
    }

    private AirportInfoResponse createAirportInfo(String icaoId, String iataId, String name) {
        AirportInfoResponse airport = new AirportInfoResponse();
        airport.setIcaoId(icaoId);
        airport.setIataId(iataId);
        airport.setName(name);
        airport.setLat(39.8617);
        airport.setLon(-104.6731);
        airport.setElev(1655.0);
        airport.setState("CO");
        airport.setCountry("US");
        return airport;
    }

    private StationInfoResponse createStationInfo(String id, String site) {
        StationInfoResponse station = new StationInfoResponse();
        station.setId(id);
        station.setSite(site);
        station.setLat(38.9697);
        station.setLon(-104.8133);
        station.setElev(2004);
        station.setState("CO");
        station.setCountry("US");
        return station;
    }
}