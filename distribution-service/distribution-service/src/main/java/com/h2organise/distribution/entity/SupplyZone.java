package com.h2organise.distribution.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// a supply zone like North Zone, South Zone etc.
// other services (resource, quality, maintenance, complaint) only store this id as zone_id
@Entity
@Table(name = "supply_zones")
@Getter
@Setter
@NoArgsConstructor
public class SupplyZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    private Integer population;

    private Integer households;

    // this points to water_sources.id in resource-service
    // it's just a number, NO foreign key because it's a different database
    @Column(name = "primary_source_id")
    private Long primarySourceId;

    // saved as text (CRITICAL) in db instead of 0,1,2 so it's easy to read
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ZoneStatus status = ZoneStatus.NORMAL;
}
