package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.PumpStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for POST /pumps and PUT /pumps/{id}
@Getter
@Setter
@NoArgsConstructor
public class PumpRequest {

    @NotBlank(message = "name is required")
    @Size(max = 255, message = "name must be at most 255 characters")
    private String name;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @Positive(message = "sourceId must be positive")
    private Long sourceId;

    @PositiveOrZero(message = "capacityLph cannot be negative")
    private Double capacityLph;

    private LocalDate installedDate;

    // optional: WORKING is used for a new pump, and the old status is kept on update when empty
    private PumpStatus status;
}
