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
@Table(name = "pumps")
@Getter
@Setter
@NoArgsConstructor
public class Pump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // supply_zones.id in distribution-service (no foreign key)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    // water_sources.id in resource-service (no foreign key)
    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "capacity_lph")
    private Double capacityLph; // litres per hour

    @Column(name = "installed_date")
    private LocalDate installedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PumpStatus status;
}
