package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.DistributionSchedule;
import com.h2organise.distribution.entity.ScheduleStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleResponse(
        Long id,
        Long zoneId,
        String zoneName,
        LocalDate supplyDate,
        LocalTime startTime,
        LocalTime endTime,
        Double plannedVolume,
        ScheduleStatus status,
        String remarks) {

    public static ScheduleResponse from(DistributionSchedule s) {
        return new ScheduleResponse(s.getId(), s.getZone().getId(), s.getZone().getName(), s.getSupplyDate(),
                s.getStartTime(), s.getEndTime(), s.getPlannedVolume(), s.getStatus(), s.getRemarks());
    }
}
