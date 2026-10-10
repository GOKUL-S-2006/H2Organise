package com.h2organise.complaint.dto;

import com.h2organise.complaint.entity.SupplyRequestStatus;
import com.h2organise.complaint.entity.SupplyRequestType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SupplyRequestRequest(
        @NotNull Long citizenId,
        @NotNull Long zoneId,
        @NotNull SupplyRequestType requestType,
        @Positive Double quantityLitres,
        LocalDate requiredDate,
        @Size(max = 255) String reason,
        SupplyRequestStatus status) {   // optional, a new request is PENDING
}
