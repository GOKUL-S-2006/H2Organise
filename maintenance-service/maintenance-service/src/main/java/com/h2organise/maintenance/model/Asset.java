package com.h2organise.maintenance.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// a pump, pipeline, tank etc.
@Entity
@Table(name = "assets")
@Getter
@Setter
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;      // PUMP, PIPELINE, TANK, VALVE ...
    private String location;
    private String status;    // WORKING, FAULTY, UNDER_REPAIR
    private LocalDate lastServiced;
}
