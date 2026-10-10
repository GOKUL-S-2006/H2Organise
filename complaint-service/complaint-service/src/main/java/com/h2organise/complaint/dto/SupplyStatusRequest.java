package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.SupplyRequestStatus;
import jakarta.validation.constraints.NotNull;

// body of PUT /supply-requests/{id}/status
public record SupplyStatusRequest(@NotNull SupplyRequestStatus status) {
}
