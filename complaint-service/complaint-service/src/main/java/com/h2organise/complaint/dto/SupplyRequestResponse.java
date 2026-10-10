package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.SupplyRequest;
import com.h2organise.complaint.entity.SupplyRequestStatus;
import com.h2organise.complaint.entity.SupplyRequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SupplyRequestResponse(
        Long id,
        Long citizenId,
        Long zoneId,
        SupplyRequestType requestType,
        Double quantityLitres,
        LocalDate requiredDate,
        String reason,
        SupplyRequestStatus status,
        LocalDateTime createdAt) {

    public static SupplyRequestResponse from(SupplyRequest r) {
        return new SupplyRequestResponse(r.getId(), r.getCitizenId(), r.getZoneId(), r.getRequestType(),
                r.getQuantityLitres(), r.getRequiredDate(), r.getReason(), r.getStatus(), r.getCreatedAt());
    }
}
