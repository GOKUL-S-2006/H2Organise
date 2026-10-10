package com.h2organise.quality.dto;

// zoneId is null when the summary is for all zones
public record ComplianceSummaryResponse(
        Long zoneId,
        long totalTests,
        long compliantTests,
        long nonCompliantTests,
        double compliancePercent) {
}
