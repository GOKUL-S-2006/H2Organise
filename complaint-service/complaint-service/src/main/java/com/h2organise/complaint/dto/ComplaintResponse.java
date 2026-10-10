package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.Complaint;
import com.h2organise.complaint.entity.ComplaintPriority;
import com.h2organise.complaint.entity.ComplaintStatus;
import com.h2organise.complaint.entity.ComplaintType;

import java.time.LocalDateTime;

public record ComplaintResponse(
        Long id,
        Long citizenId,
        Long zoneId,
        ComplaintType type,
        String title,
        String description,
        String location,
        ComplaintPriority priority,
        ComplaintStatus status,
        String assignedTo,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String resolutionNote) {

    // converts the entity into a response object
    public static ComplaintResponse from(Complaint c) {
        return new ComplaintResponse(c.getId(), c.getCitizenId(), c.getZoneId(), c.getType(), c.getTitle(),
                c.getDescription(), c.getLocation(), c.getPriority(), c.getStatus(), c.getAssignedTo(),
                c.getCreatedAt(), c.getResolvedAt(), c.getResolutionNote());
    }
}
