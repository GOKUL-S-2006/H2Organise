package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityParameter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for PUT /standards: changes the range of one parameter
@Getter
@Setter
@NoArgsConstructor
public class QualityStandardRequest {

    @NotNull(message = "parameter is required")
    private QualityParameter parameter;

    @NotNull(message = "minValue is required")
    @PositiveOrZero(message = "minValue cannot be negative")
    private Double minValue;

    @NotNull(message = "maxValue is required")
    @PositiveOrZero(message = "maxValue cannot be negative")
    private Double maxValue;
}
