package com.h2organise.quality.dto;

import java.time.LocalDate;

import com.h2organise.quality.entity.InspectionResult;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InspectionResponse {

    private Long id;
    private Long sourceId;
    private Long zoneId;
    private LocalDate inspectionDate;
    private String inspectorName;
    private InspectionResult result;
    private String remarks;
    private LocalDate nextDueDate;
}
