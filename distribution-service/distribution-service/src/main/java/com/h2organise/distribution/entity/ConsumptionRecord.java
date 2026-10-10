package com.h2organise.distribution.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// how much water a zone used on a day, split by category
@Entity
@Table(name = "consumption_records")
@Getter
@Setter
@NoArgsConstructor
public class ConsumptionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private SupplyZone zone;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConsumptionCategory category;

    // in ML (million litres)
    @Column(name = "volume_consumed", nullable = false)
    private Double volumeConsumed;
}
