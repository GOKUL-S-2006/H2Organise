package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.Pipeline;
import com.h2organise.distribution.entity.PipelineStatus;

import java.time.LocalDate;

public record PipelineResponse(
        Long id,
        String name,
        Long zoneId,
        String zoneName,   // extra field so the UI can show the zone name without another call
        Long sourceId,
        Double lengthKm,
        Integer diameterMm,
        String material,
        LocalDate installedDate,
        PipelineStatus status) {

    public static PipelineResponse from(Pipeline p) {
        return new PipelineResponse(p.getId(), p.getName(), p.getZone().getId(), p.getZone().getName(),
                p.getSourceId(), p.getLengthKm(), p.getDiameterMm(), p.getMaterial(),
                p.getInstalledDate(), p.getStatus());
    }
}
