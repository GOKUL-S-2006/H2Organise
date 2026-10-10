package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.Fault;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;

import java.time.LocalDateTime;

public record FaultResponse(
        Long id,
        AssetType assetType,
        Long assetId,
        Long zoneId,
        String description,
        FaultSeverity severity,
        FaultStatus status,
        LocalDateTime reportedAt,
        LocalDateTime resolvedAt,
        String reportedBy) {

    public static FaultResponse from(Fault f) {
        return new FaultResponse(f.getId(), f.getAssetType(), f.getAssetId(), f.getZoneId(), f.getDescription(),
                f.getSeverity(), f.getStatus(), f.getReportedAt(), f.getResolvedAt(), f.getReportedBy());
    }
}
