package com.h2organise.quality.dto;

import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.entity.QualityTest;

import java.time.LocalDate;

public record QualityTestResponse(
        Long id,
        Long sourceId,
        Long zoneId,
        LocalDate testDate,
        String testedBy,
        Double ph,
        Double turbidity,
        Double chlorine,
        Double tds,
        Boolean bacteriaPresent,
        ComplianceStatus complianceStatus,
        String remarks) {

    public static QualityTestResponse from(QualityTest t) {
        return new QualityTestResponse(t.getId(), t.getSourceId(), t.getZoneId(), t.getTestDate(), t.getTestedBy(),
                t.getPh(), t.getTurbidity(), t.getChlorine(), t.getTds(), t.getBacteriaPresent(),
                t.getComplianceStatus(), t.getRemarks());
    }
}
