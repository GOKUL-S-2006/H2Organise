package com.h2organise.maintenance.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// created when a fault is reported on an asset
@Entity
@Table(name = "maintenance_jobs")
@Getter
@Setter
public class MaintenanceJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private String problem;
    private String priority;    // LOW, MEDIUM, HIGH
    private String reportedBy;
    private LocalDate reportedDate;

    private String assignedTo;
    private String status;      // OPEN, IN_PROGRESS, DONE
    private LocalDate completedDate;
    private double cost;
    private String notes;
}
