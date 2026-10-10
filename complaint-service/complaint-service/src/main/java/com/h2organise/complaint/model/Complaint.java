package com.h2organise.complaint.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "complaints")
@Getter
@Setter
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String complaintNumber;  // e.g. CMP-1001, given to the public to track
    private String name;
    private String phone;
    private String area;
    private String category;    // WATER_SHORTAGE, LEAKAGE, DIRTY_WATER, LOW_PRESSURE, OTHER
    private String description;

    private String priority;    // LOW, MEDIUM, HIGH
    private String status;      // OPEN, IN_PROGRESS, RESOLVED
    private String assignedTo;
    private String remarks;

    private LocalDate createdDate;
    private LocalDate resolvedDate;
}
