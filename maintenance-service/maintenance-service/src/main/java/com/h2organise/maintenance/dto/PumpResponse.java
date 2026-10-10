package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.PumpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PumpResponse {

    private Long id;
    private String name;
    private Long zoneId;
    private Long sourceId;
    private Double capacityLph;
    private LocalDate installedDate;
    private PumpStatus status;
}
