package com.h2organise.quality.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quality_tests")
@Getter
@Setter
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private LocalDate testDate;
    private String testedBy;

    @Enumerated(EnumType.STRING)
    private WaterUse waterUse;

    private double ph;
    private double turbidity;
    private double tds;

    private String result;   // PASS or FAIL
    private String reason;   // filled only when FAIL
}
