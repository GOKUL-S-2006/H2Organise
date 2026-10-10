package com.h2organise.quality.dto;

import java.time.LocalDate;

import com.h2organise.quality.entity.InspectionResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InspectionRequest {

    @Positive(message = "sourceId must be positive")
    private Long sourceId;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @NotNull(message = "inspectionDate is required")
    private LocalDate inspectionDate;

    @Size(max = 255, message = "inspectorName must be at most 255 characters")
    private String inspectorName;

    @NotNull(message = "result is required")
    private InspectionResult result;

    @Size(max = 1000, message = "remarks must be at most 1000 characters")
    private String remarks;

    private LocalDate nextDueDate;
}
