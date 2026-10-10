package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleRequest(
        @NotNull Long zoneId,
        @NotNull LocalDate supplyDate,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull @Positive Double plannedVolume,
        ScheduleStatus status,
        @Size(max = 255) String remarks) {
}
