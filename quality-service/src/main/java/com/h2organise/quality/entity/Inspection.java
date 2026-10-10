package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspector_name")
    private String inspectorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result;

    private String remarks;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    public enum Result {
        PASSED,
        FAILED,
        ACTION_REQUIRED
    }
}
