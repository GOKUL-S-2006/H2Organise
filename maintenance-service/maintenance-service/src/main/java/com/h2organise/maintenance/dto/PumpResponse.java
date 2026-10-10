package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.Pump;
import com.h2organise.maintenance.entity.PumpStatus;

import java.time.LocalDate;

public record PumpResponse(
        Long id,
        String name,
        Long zoneId,
        Long sourceId,
        Double capacityLph,
        LocalDate installedDate,
        PumpStatus status) {

    public static PumpResponse from(Pump p) {
        return new PumpResponse(p.getId(), p.getName(), p.getZoneId(), p.getSourceId(),
                p.getCapacityLph(), p.getInstalledDate(), p.getStatus());
    }
}
