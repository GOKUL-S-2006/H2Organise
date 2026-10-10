package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.ZoneStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// data the client sends when creating or updating a zone
public record ZoneRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 255) String description,
        @PositiveOrZero Integer population,
        @PositiveOrZero Integer households,
        Long primarySourceId,
        ZoneStatus status) {
}
