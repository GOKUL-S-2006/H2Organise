package com.h2organise.resource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// one measurement of a source's water level (taken every day or so)
@Entity
@Table(name = "level_readings")
@Getter
@Setter
@NoArgsConstructor
public class LevelReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // real foreign key because water_sources is in the same database
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private WaterSource source;

    @Column(name = "reading_time", nullable = false)
    private LocalDateTime readingTime;

    // in ML (million litres)
    @Column(name = "current_volume", nullable = false)
    private Double currentVolume;

    // calculated by the service: currentVolume / totalCapacity * 100
    @Column(name = "level_percent", nullable = false)
    private Double levelPercent;

    // ML per day
    @Column(name = "inflow_rate")
    private Double inflowRate;

    @Column(name = "outflow_rate")
    private Double outflowRate;

    @Column(name = "recorded_by", length = 100)
    private String recordedBy;
}
