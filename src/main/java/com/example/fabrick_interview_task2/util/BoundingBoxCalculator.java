package com.example.fabrick_interview_task2.util;

import com.example.fabrick_interview_task2.model.BoundingBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoundingBoxCalculator {

    public static BoundingBox calculateBBox(double latitude, double longitude, double closestBy) {
        double minLat = latitude - closestBy;
        double maxLat = latitude + closestBy;
        double minLon = longitude - closestBy;
        double maxLon = longitude + closestBy;

        log.debug("Bounding box: minLat={}, minLon={}, maxLat={}, maxLon={}", minLat, minLon, maxLat, maxLon);
        return new BoundingBox(minLat, minLon, maxLat, maxLon);
    }
}
