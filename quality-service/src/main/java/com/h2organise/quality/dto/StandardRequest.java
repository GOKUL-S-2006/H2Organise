package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityStandard.Parameter;
import jakarta.validation.constraints.NotNull;

public record StandardRequest(
        @NotNull(message = "parameter is required (PH, TURBIDITY, CHLORINE or TDS)") Parameter parameter,
        Double minValue,
        Double maxValue) {
}
