package com.h2organise.quality.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for POST /tests and PUT /tests/{id}. complianceStatus is not sent; the service works it out.
@Getter
@Setter
@NoArgsConstructor
public class QualityTestRequest {

    @Positive(message = "sourceId must be positive")
    private Long sourceId;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @NotNull(message = "testDate is required")
    private LocalDate testDate;

    @Size(max = 255, message = "testedBy must be at most 255 characters")
    private String testedBy;

    @PositiveOrZero(message = "ph cannot be negative")
    private Double ph;

    @PositiveOrZero(message = "turbidity cannot be negative")
    private Double turbidity;

    @PositiveOrZero(message = "chlorine cannot be negative")
    private Double chlorine;

    @PositiveOrZero(message = "tds cannot be negative")
    private Double tds;

    private Boolean bacteriaPresent;

    @Size(max = 1000, message = "remarks must be at most 1000 characters")
    private String remarks;
}
