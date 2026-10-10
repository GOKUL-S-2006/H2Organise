package com.h2organise.resource.dto;

import com.h2organise.resource.entity.SourceStatus;
import com.h2organise.resource.entity.SourceType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

// data the client sends when creating or updating a source
public record SourceRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull SourceType type,
        @Size(max = 200) String location,
        BigDecimal latitude,
        BigDecimal longitude,
        @NotNull @Positive Double totalCapacity,
        @DecimalMin("0.0") @DecimalMax("100.0") Double minThresholdPercent,
        Long zoneId,
        SourceStatus status,
        LocalDate commissionedDate) {
}
