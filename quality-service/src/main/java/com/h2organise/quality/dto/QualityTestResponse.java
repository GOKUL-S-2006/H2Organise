package com.h2organise.quality.dto;

import java.time.LocalDate;

import com.h2organise.quality.entity.ComplianceStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QualityTestResponse {

    private Long id;
    private Long sourceId;
    private Long zoneId;
    private LocalDate testDate;
    private String testedBy;
    private Double ph;
    private Double turbidity;
    private Double chlorine;
    private Double tds;
    private Boolean bacteriaPresent;
    private ComplianceStatus complianceStatus;
    private String remarks;
}
