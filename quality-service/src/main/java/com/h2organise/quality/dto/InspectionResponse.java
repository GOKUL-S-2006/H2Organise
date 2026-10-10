package com.h2organise.quality.dto;

import com.h2organise.quality.entity.Inspection;
import com.h2organise.quality.entity.Inspection.Result;

import java.time.LocalDate;

public record InspectionResponse(
        Long id,
        Long sourceId,
        Long zoneId,
        LocalDate inspectionDate,
        String inspectorName,
        Result result,
        String remarks,
        LocalDate nextDueDate) {

    public static InspectionResponse from(Inspection i) {
        return new InspectionResponse(i.getId(), i.getSourceId(), i.getZoneId(), i.getInspectionDate(),
                i.getInspectorName(), i.getResult(), i.getRemarks(), i.getNextDueDate());
    }
}
