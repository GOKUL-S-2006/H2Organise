package com.h2organise.maintenance.dto;

import java.time.LocalDateTime;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FaultResponse {

    private Long id;
    private AssetType assetType;
    private Long assetId;
    private Long zoneId;
    private String description;
    private FaultSeverity severity;
    private FaultStatus status;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private String reportedBy;
}
