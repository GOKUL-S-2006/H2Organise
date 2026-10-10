package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.ConsumptionCategory;
import com.h2organise.distribution.entity.ConsumptionRecord;

import java.time.LocalDate;

public record ConsumptionResponse(
        Long id,
        Long zoneId,
        LocalDate recordDate,
        ConsumptionCategory category,
        Double volumeConsumed) {

    public static ConsumptionResponse from(ConsumptionRecord c) {
        return new ConsumptionResponse(c.getId(), c.getZone().getId(), c.getRecordDate(),
                c.getCategory(), c.getVolumeConsumed());
    }
}
