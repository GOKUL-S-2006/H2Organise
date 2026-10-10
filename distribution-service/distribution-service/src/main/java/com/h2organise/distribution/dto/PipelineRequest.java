package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.PipelineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PipelineRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Long zoneId,
        Long sourceId,
        @Positive Double lengthKm,
        @Positive Integer diameterMm,
        @Size(max = 30) String material,
        LocalDate installedDate,
        PipelineStatus status) {
}
