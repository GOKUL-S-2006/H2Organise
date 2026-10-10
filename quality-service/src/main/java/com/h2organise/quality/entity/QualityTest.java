package com.h2organise.quality.entity;

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
@Table(name = "quality_tests")
@Getter
@Setter
@NoArgsConstructor
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // water_sources.id in resource-service (optional, no foreign key)
    @Column(name = "source_id")
    private Long sourceId;

    // supply_zones.id in distribution-service (no foreign key)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "tested_by")
    private String testedBy;

    private Double ph;
    private Double turbidity; // NTU
    private Double chlorine;  // mg/L
    private Double tds;       // mg/L

    @Column(name = "bacteria_present")
    private Boolean bacteriaPresent;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", nullable = false, length = 20)
    private ComplianceStatus complianceStatus;

    @Column(length = 1000)
    private String remarks;
}
