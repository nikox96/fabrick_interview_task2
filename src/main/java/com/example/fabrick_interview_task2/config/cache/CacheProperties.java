package com.example.fabrick_interview_task2.config.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.cache.caffeine")
@Getter
@Setter
public class CacheProperties {

    private long infoTtlHours;
    private int infoMaxSize;
    private long bboxTtlMinutes;
    private int bboxMaxSize;
}