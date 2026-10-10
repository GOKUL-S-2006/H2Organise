package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "quality_tests")
@Getter
@Setter
@NoArgsConstructor
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // water source in resource-service, optional, no FK
    @Column(name = "source_id")
    private Long sourceId;

    // supply zone in distribution-service, plain number only (no FK)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "tested_by", length = 100)
    private String testedBy;

    private Double ph;
    private Double turbidity; // NTU
    private Double chlorine;  // mg/L
    private Double tds;       // mg/L

    @Column(name = "bacteria_present")
    private Boolean bacteriaPresent;

    // set by the service, never sent by the client
    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", nullable = false, length = 20)
    private ComplianceStatus complianceStatus;

    @Column(length = 1000)
    private String remarks;
}
