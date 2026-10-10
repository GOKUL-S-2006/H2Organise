package com.h2organise.quality.dto;

import com.h2organise.quality.entity.Inspection.Result;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InspectionRequest(
        @NotNull(message = "sourceId is required") Long sourceId,
        @NotNull(message = "zoneId is required") Long zoneId,
        @NotNull(message = "inspectionDate is required") LocalDate inspectionDate,
        @NotBlank(message = "inspectorName is required")
        @Size(max = 255, message = "inspectorName must be at most 255 characters") String inspectorName,
        @NotNull(message = "result is required (PASSED, FAILED or ACTION_REQUIRED)") Result result,
        @Size(max = 255, message = "remarks must be at most 255 characters") String remarks,
        LocalDate nextDueDate) {
}
