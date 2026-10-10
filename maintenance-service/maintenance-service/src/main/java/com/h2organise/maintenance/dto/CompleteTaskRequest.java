package com.h2organise.maintenance.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Optional body for PUT /tasks/{id}/complete
@Getter
@Setter
@NoArgsConstructor
public class CompleteTaskRequest {

    @PositiveOrZero(message = "cost cannot be negative")
    private Double cost;

    @Size(max = 1000, message = "remarks must be at most 1000 characters")
    private String remarks;
}
