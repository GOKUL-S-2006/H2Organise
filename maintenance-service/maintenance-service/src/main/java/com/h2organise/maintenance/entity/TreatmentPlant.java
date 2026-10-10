package com.h2organise.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "treatment_plants")
@Getter
@Setter
@NoArgsConstructor
public class TreatmentPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // supply zone in distribution-service, plain number only (no FK)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    // million litres per day
    @Column(name = "capacity_mld")
    private Double capacityMld;

    @Column(name = "last_serviced_date")
    private LocalDate lastServicedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlantStatus status = PlantStatus.OPERATIONAL;
}
