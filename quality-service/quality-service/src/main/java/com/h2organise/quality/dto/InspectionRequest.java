package com.h2organise.quality.dto;

import com.h2organise.quality.entity.InspectionResult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InspectionRequest(
        Long sourceId,
        @NotNull Long zoneId,
        @NotNull LocalDate inspectionDate,
        @Size(max = 100) String inspectorName,
        @NotNull InspectionResult result,
        @Size(max = 1000) String remarks,
        LocalDate nextDueDate) {
}
