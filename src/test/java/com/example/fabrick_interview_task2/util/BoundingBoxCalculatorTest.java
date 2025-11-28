package com.example.fabrick_interview_task2.util;

import com.example.fabrick_interview_task2.model.BoundingBox;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoundingBoxCalculatorTest {

    @Test
    void calculateBBox_withPositiveCoordinates_shouldReturnCorrectBoundingBox() {
        double latitude = 2.0;
        double longitude = 2.0;
        double closestBy = 1.0;

        BoundingBox result = BoundingBoxCalculator.calculateBBox(latitude, longitude, closestBy);

        assertThat(result).isNotNull();
        assertThat(result.getMinLatitude()).isEqualTo(1.0);
        assertThat(result.getMaxLatitude()).isEqualTo(3.0);
        assertThat(result.getMinLongitude()).isEqualTo(1.0);
        assertThat(result.getMaxLongitude()).isEqualTo(3.0);
    }

    @Test
    void calculateBBox_withZeroClosestBy_shouldReturnBoundingBoxWithSameCoordinates() {
        double latitude = 2.0;
        double longitude = 2.0;
        double closestBy = 0.0;

        BoundingBox result = BoundingBoxCalculator.calculateBBox(latitude, longitude, closestBy);

        assertThat(result).isNotNull();
        assertThat(result.getMinLatitude()).isEqualTo(2.0);
        assertThat(result.getMaxLatitude()).isEqualTo(2.0);
        assertThat(result.getMinLongitude()).isEqualTo(2.0);
        assertThat(result.getMaxLongitude()).isEqualTo(2.0);
    }
}