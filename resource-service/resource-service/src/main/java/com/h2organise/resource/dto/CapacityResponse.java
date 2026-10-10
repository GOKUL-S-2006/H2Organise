package com.h2organise.resource.dto;

import java.time.LocalDateTime;

// used by /sources/{id}/capacity
// the reading fields are null if the source has no reading yet
public record CapacityResponse(
        Long sourceId,
        String name,
        Double totalCapacity,
        Double currentVolume,
        Double availableCapacity,
        Double levelPercent,
        Double minThresholdPercent,
        LocalDateTime readingTime) {
}
