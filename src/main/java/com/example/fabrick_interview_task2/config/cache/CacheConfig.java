package com.example.fabrick_interview_task2.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                buildCache("airportInfo", cacheProperties.getInfoTtlHours(), TimeUnit.HOURS, cacheProperties.getInfoMaxSize()),
                buildCache("stationInfo", cacheProperties.getInfoTtlHours(), TimeUnit.HOURS, cacheProperties.getInfoMaxSize()),
                buildCache("stationsBbox", cacheProperties.getBboxTtlMinutes(), TimeUnit.MINUTES, cacheProperties.getBboxMaxSize()),
                buildCache("airportsBbox", cacheProperties.getBboxTtlMinutes(), TimeUnit.MINUTES, cacheProperties.getBboxMaxSize())
        ));
        return cacheManager;
    }

    private CaffeineCache buildCache(String name, long duration, TimeUnit timeUnit, int maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(duration, timeUnit)
                .maximumSize(maxSize)
                .recordStats()
                .build());
    }
}