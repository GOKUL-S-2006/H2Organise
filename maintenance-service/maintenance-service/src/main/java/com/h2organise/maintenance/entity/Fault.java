package com.h2organise.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "faults")
@Getter
@Setter
@NoArgsConstructor
public class Fault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false, length = 20)
    private AssetType assetType;

    // pump or plant id from this db, or pipeline id from distribution-service, so no FK
    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    // supply zone in distribution-service, plain number only (no FK)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FaultSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FaultStatus status = FaultStatus.OPEN;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "reported_by", length = 100)
    private String reportedBy;
}
