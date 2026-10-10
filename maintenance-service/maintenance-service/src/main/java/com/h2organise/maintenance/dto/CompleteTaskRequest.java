package com.h2organise.maintenance.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// optional body for PUT /tasks/{id}/complete
public record CompleteTaskRequest(
        @PositiveOrZero Double cost,
        @Size(max = 1000) String remarks) {
}
