package com.h2organise.quality.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Input for POST /tests and PUT /tests/{id}. complianceStatus is NOT accepted: the service decides it. */
public record QualityTestRequest(
        Long sourceId,
        @NotNull(message = "zoneId is required") Long zoneId,
        @NotNull(message = "testDate is required") LocalDate testDate,
        @Size(max = 255, message = "testedBy must be at most 255 characters") String testedBy,
        @DecimalMin(value = "0.0", message = "ph must be between 0 and 14")
        @DecimalMax(value = "14.0", message = "ph must be between 0 and 14") Double ph,
        @DecimalMin(value = "0.0", message = "turbidity cannot be negative") Double turbidity,
        @DecimalMin(value = "0.0", message = "chlorine cannot be negative") Double chlorine,
        @DecimalMin(value = "0.0", message = "tds cannot be negative") Double tds,
        Boolean bacteriaPresent,
        @Size(max = 255, message = "remarks must be at most 255 characters") String remarks) {
}
