package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.ConsumptionCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record ConsumptionRequest(
        @NotNull Long zoneId,
        @NotNull LocalDate recordDate,
        @NotNull ConsumptionCategory category,
        @NotNull @PositiveOrZero Double volumeConsumed) {
}
