package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// body of PUT /complaints/{id}/status
public record ComplaintStatusRequest(
        @NotNull ComplaintStatus status,
        @Size(max = 255) String comment,       // shown in the history, used as resolution note when RESOLVED
        @Size(max = 100) String assignedTo,    // needed when status is ASSIGNED
        @Size(max = 100) String updatedBy) {
}
