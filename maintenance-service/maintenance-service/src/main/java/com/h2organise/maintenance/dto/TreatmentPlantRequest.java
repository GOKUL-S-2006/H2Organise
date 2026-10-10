package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.PlantStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for POST /plants and PUT /plants/{id}
@Getter
@Setter
@NoArgsConstructor
public class TreatmentPlantRequest {

    @NotBlank(message = "name is required")
    @Size(max = 255, message = "name must be at most 255 characters")
    private String name;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @PositiveOrZero(message = "capacityMld cannot be negative")
    private Double capacityMld;

    private LocalDate lastServicedDate;

    // optional: OPERATIONAL is used for a new plant, and the old status is kept on update when empty
    private PlantStatus status;
}
