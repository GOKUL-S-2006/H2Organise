package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.PlantStatus;
import com.h2organise.maintenance.entity.TreatmentPlant;

import java.time.LocalDate;

public record TreatmentPlantResponse(
        Long id,
        String name,
        Long zoneId,
        Double capacityMld,
        LocalDate lastServicedDate,
        PlantStatus status) {

    public static TreatmentPlantResponse from(TreatmentPlant p) {
        return new TreatmentPlantResponse(p.getId(), p.getName(), p.getZoneId(), p.getCapacityMld(),
                p.getLastServicedDate(), p.getStatus());
    }
}
