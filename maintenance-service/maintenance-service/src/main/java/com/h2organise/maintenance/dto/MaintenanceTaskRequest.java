package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.AssetType;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.entity.TaskType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Body for POST /tasks and PUT /tasks/{id}
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceTaskRequest {

    // optional: leave empty for preventive work
    @Positive(message = "faultId must be positive")
    private Long faultId;

    @NotNull(message = "assetType is required")
    private AssetType assetType;

    @NotNull(message = "assetId is required")
    @Positive(message = "assetId must be positive")
    private Long assetId;

    @NotNull(message = "zoneId is required")
    @Positive(message = "zoneId must be positive")
    private Long zoneId;

    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be at most 255 characters")
    private String title;

    @NotNull(message = "taskType is required")
    private TaskType taskType;

    @Size(max = 255, message = "assignedTo must be at most 255 characters")
    private String assignedTo;

    private LocalDate scheduledDate;

    // optional: PENDING is used for a new task, and the old status is kept on update when empty
    private TaskStatus status;

    @PositiveOrZero(message = "cost cannot be negative")
    private Double cost;

    @Size(max = 1000, message = "remarks must be at most 1000 characters")
    private String remarks;
}
