package com.h2organise.maintenance.dto;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.MaintenanceTask;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.entity.TaskType;

import java.time.LocalDate;

public record MaintenanceTaskResponse(
        Long id,
        Long faultId,
        AssetType assetType,
        Long assetId,
        Long zoneId,
        String title,
        TaskType taskType,
        String assignedTo,
        LocalDate scheduledDate,
        LocalDate completedDate,
        TaskStatus status,
        Double cost,
        String remarks) {

    public static MaintenanceTaskResponse from(MaintenanceTask t) {
        Long faultId = t.getFault() != null ? t.getFault().getId() : null;
        return new MaintenanceTaskResponse(t.getId(), faultId, t.getAssetType(), t.getAssetId(), t.getZoneId(),
                t.getTitle(), t.getTaskType(), t.getAssignedTo(), t.getScheduledDate(), t.getCompletedDate(),
                t.getStatus(), t.getCost(), t.getRemarks());
    }
}
