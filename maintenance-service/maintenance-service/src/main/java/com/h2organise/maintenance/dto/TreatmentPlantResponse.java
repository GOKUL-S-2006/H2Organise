package com.h2organise.maintenance.dto;

import java.time.LocalDate;

import com.h2organise.maintenance.entity.PlantStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TreatmentPlantResponse {

    private Long id;
    private String name;
    private Long zoneId;
    private Double capacityMld;
    private LocalDate lastServicedDate;
    private PlantStatus status;
}
