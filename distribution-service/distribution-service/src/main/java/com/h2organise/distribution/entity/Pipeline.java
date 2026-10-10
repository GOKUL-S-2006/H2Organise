package com.h2organise.distribution.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pipelines")
@Getter
@Setter
@NoArgsConstructor
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // real foreign key because supply_zones is in the same database
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private SupplyZone zone;

    // water source in resource-service, plain number only (no FK)
    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "length_km")
    private Double lengthKm;

    @Column(name = "diameter_mm")
    private Integer diameterMm;

    // PVC / Iron / Steel
    @Column(length = 30)
    private String material;

    @Column(name = "installed_date")
    private LocalDate installedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PipelineStatus status = PipelineStatus.ACTIVE;
}
