package com.h2organise.resource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// a reservoir, lake, well, tank or river that supplies water
@Entity
@Table(name = "water_sources")
@Getter
@Setter
@NoArgsConstructor
public class WaterSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SourceType type;

    @Column(length = 200)
    private String location;

    // optional, only needed if we show a map
    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    // in ML (million litres)
    @Column(name = "total_capacity", nullable = false)
    private Double totalCapacity;

    // if the level goes below this percent the source is critical
    @Column(name = "min_threshold_percent", nullable = false)
    private Double minThresholdPercent = 30.0;

    // which zone this source mainly serves, comes from distribution-service
    // it's just a number, NO foreign key because it's a different database
    @Column(name = "zone_id")
    private Long zoneId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SourceStatus status = SourceStatus.ACTIVE;

    @Column(name = "commissioned_date")
    private LocalDate commissionedDate;
}
