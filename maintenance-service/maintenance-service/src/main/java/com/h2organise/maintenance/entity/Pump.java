package com.h2organise.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pumps")
@Getter
@Setter
@NoArgsConstructor
public class Pump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // supply zone in distribution-service, plain number only (no FK)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    // water source in resource-service, plain number only (no FK)
    @Column(name = "source_id")
    private Long sourceId;

    // litres per hour
    @Column(name = "capacity_lph")
    private Double capacityLph;

    @Column(name = "installed_date")
    private LocalDate installedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PumpStatus status = PumpStatus.WORKING;
}
