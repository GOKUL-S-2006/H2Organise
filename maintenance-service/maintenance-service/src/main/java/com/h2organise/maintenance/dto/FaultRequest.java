package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FaultRequest(
        @NotNull AssetType assetType,
        @NotNull Long assetId,
        @NotNull Long zoneId,
        @Size(max = 1000) String description,
        @NotNull FaultSeverity severity,
        @Size(max = 100) String reportedBy,
        FaultStatus status) {
}
