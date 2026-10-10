package com.h2organise.resource.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// data sent when recording a new reading
// levelPercent is NOT sent, the service calculates it
public record LevelRequest(
        @NotNull Long sourceId,
        LocalDateTime readingTime,   // optional, we use the current time if it's missing
        @NotNull @PositiveOrZero Double currentVolume,
        @PositiveOrZero Double inflowRate,
        @PositiveOrZero Double outflowRate,
        @Size(max = 100) String recordedBy) {
}
