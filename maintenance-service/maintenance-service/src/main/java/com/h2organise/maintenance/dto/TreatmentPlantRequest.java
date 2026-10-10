package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.PlantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TreatmentPlantRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Long zoneId,
        @Positive Double capacityMld,
        LocalDate lastServicedDate,
        PlantStatus status) {
}
