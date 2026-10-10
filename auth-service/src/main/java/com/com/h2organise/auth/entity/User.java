package com.h2organise.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// a person who can log in (admin, engineer, operator or citizen)
// table is called "users" because "user" is a reserved word in postgres
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // used to log in, always stored in lower case
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // BCrypt hash, never the real password
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    // the citizen's or operator's zone, comes from distribution-service
    // it's just a number, NO foreign key because it's a different database
    @Column(name = "zone_id")
    private Long zoneId;

    // false means the account is deactivated and can't log in
    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
