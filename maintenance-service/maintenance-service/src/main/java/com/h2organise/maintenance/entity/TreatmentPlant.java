package com.h2organise.maintenance.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "treatment_plants")
@Getter
@Setter
@NoArgsConstructor
public class TreatmentPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // supply_zones.id in distribution-service (no foreign key)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "capacity_mld")
    private Double capacityMld; // million litres per day

    @Column(name = "last_serviced_date")
    private LocalDate lastServicedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlantStatus status;
}
