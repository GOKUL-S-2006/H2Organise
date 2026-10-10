package com.h2organise.quality.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Result of GET /compliance/summary. zoneId is null for the overall summary.
@Getter
@Setter
@NoArgsConstructor
public class ComplianceSummaryResponse {

    private Long zoneId;
    private long totalTests;
    private long compliantTests;
    private long nonCompliantTests;
    private double compliancePercent;
}
