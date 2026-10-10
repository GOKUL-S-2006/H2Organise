package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.entity.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MaintenanceTaskRequest(
        Long faultId,   // empty for preventive work
        @NotNull AssetType assetType,
        @NotNull Long assetId,
        @NotNull Long zoneId,
        @NotBlank @Size(max = 200) String title,
        @NotNull TaskType taskType,
        @Size(max = 100) String assignedTo,
        LocalDate scheduledDate,
        TaskStatus status,
        @PositiveOrZero Double cost,
        @Size(max = 1000) String remarks) {
}
