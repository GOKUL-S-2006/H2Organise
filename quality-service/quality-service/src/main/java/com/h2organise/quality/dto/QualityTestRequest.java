package com.h2organise.quality.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// complianceStatus is not sent, the service works it out
public record QualityTestRequest(
        Long sourceId,
        @NotNull Long zoneId,
        @NotNull LocalDate testDate,
        @Size(max = 100) String testedBy,
        @PositiveOrZero Double ph,
        @PositiveOrZero Double turbidity,
        @PositiveOrZero Double chlorine,
        @PositiveOrZero Double tds,
        Boolean bacteriaPresent,
        @Size(max = 1000) String remarks) {
}
