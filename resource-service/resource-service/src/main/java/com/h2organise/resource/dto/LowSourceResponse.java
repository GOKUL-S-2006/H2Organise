package com.h2organise.resource.dto;

import java.time.LocalDateTime;

// used by /sources/low, a source whose latest level is under its limit
public record LowSourceResponse(
        Long sourceId,
        String name,
        Long zoneId,
        Double levelPercent,
        Double minThresholdPercent,
        LocalDateTime readingTime) {
}
