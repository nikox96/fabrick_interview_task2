package com.example.fabrick_interview_task2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.aviation-weather.api")
public class AviationWeatherApiProperties {
    private String baseUrl;
}