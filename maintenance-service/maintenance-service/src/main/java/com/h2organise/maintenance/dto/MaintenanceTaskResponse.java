package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.entity.TaskType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaintenanceTaskResponse {

    private Long id;
    private Long faultId;
    private AssetType assetType;
    private Long assetId;
    private Long zoneId;
    private String title;
    private TaskType taskType;
    private String assignedTo;
    private LocalDate scheduledDate;
    private LocalDate completedDate;
    private TaskStatus status;
    private Double cost;
    private String remarks;
}
