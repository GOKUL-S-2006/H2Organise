package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for POST /faults and PUT /faults/{id}
@Getter
@Setter
@NoArgsConstructor
public class FaultRequest {

    @NotNull(message = "assetType is required")
    private AssetType assetType;

    @NotNull(message = "assetId is required")
    @Positive(message = "assetId must be positive")
    private Long assetId;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @Size(max = 1000, message = "description must be at most 1000 characters")
    private String description;

    @NotNull(message = "severity is required")
    private FaultSeverity severity;

    @Size(max = 255, message = "reportedBy must be at most 255 characters")
    private String reportedBy;

    // optional: OPEN is used for a new fault, and the old status is kept on update when empty
    private FaultStatus status;
}
