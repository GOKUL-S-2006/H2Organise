package com.h2organise.resource.dto;

import com.h2organise.resource.entity.LevelReading;

import java.time.LocalDateTime;

public record LevelResponse(
        Long id,
        Long sourceId,
        String sourceName,
        LocalDateTime readingTime,
        Double currentVolume,
        Double levelPercent,
        Double inflowRate,
        Double outflowRate,
        Double availableCapacity,   // total capacity minus current volume
        String recordedBy) {

    public static LevelResponse from(LevelReading r) {
        double available = r.getSource().getTotalCapacity() - r.getCurrentVolume();
        return new LevelResponse(r.getId(), r.getSource().getId(), r.getSource().getName(), r.getReadingTime(),
                r.getCurrentVolume(), r.getLevelPercent(), r.getInflowRate(), r.getOutflowRate(),
                available, r.getRecordedBy());
    }
}
