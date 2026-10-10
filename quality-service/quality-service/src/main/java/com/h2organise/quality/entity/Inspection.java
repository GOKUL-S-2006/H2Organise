package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inspections")
@Getter
@Setter
@NoArgsConstructor
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // water source in resource-service, plain number only (no FK)
    @Column(name = "source_id")
    private Long sourceId;

    // supply zone in distribution-service, plain number only (no FK)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspector_name", length = 100)
    private String inspectorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InspectionResult result;

    @Column(length = 1000)
    private String remarks;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;
}
