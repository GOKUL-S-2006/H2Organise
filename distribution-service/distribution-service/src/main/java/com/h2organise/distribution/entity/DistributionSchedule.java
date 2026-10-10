package com.h2organise.distribution.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// when and how much water a zone gets on a given day
@Entity
@Table(name = "distribution_schedules")
@Getter
@Setter
@NoArgsConstructor
public class DistributionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private SupplyZone zone;

    @Column(name = "supply_date", nullable = false)
    private LocalDate supplyDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // in ML (million litres)
    @Column(name = "planned_volume", nullable = false)
    private Double plannedVolume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduleStatus status = ScheduleStatus.PLANNED;

    @Column(length = 255)
    private String remarks;
}
