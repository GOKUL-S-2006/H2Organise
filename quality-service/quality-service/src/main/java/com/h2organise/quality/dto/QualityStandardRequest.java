package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityParameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// body for PUT /standards, e.g. { "parameter": "PH", "minValue": 6.5, "maxValue": 8.5 }
public record QualityStandardRequest(
        @NotNull QualityParameter parameter,
        @NotNull @PositiveOrZero Double minValue,
        @NotNull @PositiveOrZero Double maxValue) {
}
