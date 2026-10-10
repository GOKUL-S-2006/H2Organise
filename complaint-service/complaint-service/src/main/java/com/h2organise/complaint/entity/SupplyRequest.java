package com.h2organise.complaint.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// a citizen asking for a tanker, extra supply or a new connection
@Entity
@Table(name = "supply_requests")
@Getter
@Setter
@NoArgsConstructor
public class SupplyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // users.id in auth-service (no foreign key)
    @Column(name = "citizen_id", nullable = false)
    private Long citizenId;

    // supply_zones.id in distribution-service (no foreign key)
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false, length = 20)
    private SupplyRequestType requestType;

    // optional, a new connection doesn't need a quantity
    @Column(name = "quantity_litres")
    private Double quantityLitres;

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupplyRequestStatus status = SupplyRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
