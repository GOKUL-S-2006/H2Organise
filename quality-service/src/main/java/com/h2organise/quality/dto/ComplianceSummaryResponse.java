package com.h2organise.quality.dto;

/** compliancePercent is a plain number: 80.0 means 80 percent. It is 0.0 when there are no tests yet. */
public record ComplianceSummaryResponse(
        Long zoneId,
        long totalTests,
        long compliantTests,
        long nonCompliantTests,
        double compliancePercent) {
}
