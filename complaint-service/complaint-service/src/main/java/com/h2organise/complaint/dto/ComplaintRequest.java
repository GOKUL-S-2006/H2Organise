package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.ComplaintPriority;
import com.h2organise.complaint.entity.ComplaintType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// data the citizen sends when registering (or the engineer when editing) a complaint
// status is NOT here, it is changed with PUT /complaints/{id}/status
public record ComplaintRequest(
        @NotNull Long citizenId,
        @NotNull Long zoneId,
        @NotNull ComplaintType type,
        @NotBlank @Size(max = 150) String title,
        @Size(max = 2000) String description,
        @Size(max = 200) String location,
        ComplaintPriority priority) {   // optional, we pick one from the type if it's missing
}
