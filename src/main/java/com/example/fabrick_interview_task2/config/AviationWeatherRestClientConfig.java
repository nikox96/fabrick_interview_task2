package com.example.fabrick_interview_task2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AviationWeatherRestClientConfig {

    @Bean
    public RestClient restClient(@Autowired AviationWeatherApiProperties apiProperties) {
        return RestClient.builder()
                .baseUrl(apiProperties.getBaseUrl())
                .build();
    }
}
