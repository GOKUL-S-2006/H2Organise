package com.h2organise.resource.dto;

import com.h2organise.resource.entity.SourceStatus;
import com.h2organise.resource.entity.SourceType;
import com.h2organise.resource.entity.WaterSource;

import java.math.BigDecimal;
import java.time.LocalDate;

// data we send back for a source
public record SourceResponse(
        Long id,
        String name,
        SourceType type,
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        Double totalCapacity,
        Double minThresholdPercent,
        Long zoneId,
        SourceStatus status,
        LocalDate commissionedDate) {

    // converts the entity into a response object
    public static SourceResponse from(WaterSource s) {
        return new SourceResponse(s.getId(), s.getName(), s.getType(), s.getLocation(), s.getLatitude(),
                s.getLongitude(), s.getTotalCapacity(), s.getMinThresholdPercent(), s.getZoneId(),
                s.getStatus(), s.getCommissionedDate());
    }
}
