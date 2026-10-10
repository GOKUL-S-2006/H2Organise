package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.ComplaintStatus;
import com.h2organise.complaint.entity.ComplaintUpdate;

import java.time.LocalDateTime;

public record ComplaintUpdateResponse(
        Long id,
        Long complaintId,
        ComplaintStatus status,
        String comment,
        String updatedBy,
        LocalDateTime updatedAt) {

    public static ComplaintUpdateResponse from(ComplaintUpdate u) {
        return new ComplaintUpdateResponse(u.getId(), u.getComplaint().getId(), u.getStatus(),
                u.getComment(), u.getUpdatedBy(), u.getUpdatedAt());
    }
}
