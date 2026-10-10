package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.PumpStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PumpRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Long zoneId,
        Long sourceId,
        @Positive Double capacityLph,
        LocalDate installedDate,
        PumpStatus status) {
}
