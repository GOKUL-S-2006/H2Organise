package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/** One laboratory or field water sample. complianceStatus is always set by the service. */
@Entity
@Table(name = "quality_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to water_sources.id in resource-service (no foreign key). */
    @Column(name = "source_id")
    private Long sourceId;

    /** Reference to supply_zones.id in distribution-service (no foreign key). */
    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "tested_by")
    private String testedBy;

    private Double ph;
    private Double turbidity;
    private Double chlorine;
    private Double tds;

    @Column(name = "bacteria_present")
    private Boolean bacteriaPresent;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", length = 20)
    private ComplianceStatus complianceStatus;

    private String remarks;

    public enum ComplianceStatus {
        COMPLIANT,
        NON_COMPLIANT
    }
}
