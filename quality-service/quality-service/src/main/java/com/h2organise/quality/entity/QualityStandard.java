package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// allowed range for one parameter, e.g. pH 6.5 to 8.5
@Entity
@Table(name = "quality_standards")
@Getter
@Setter
@NoArgsConstructor
public class QualityStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private QualityParameter parameter;

    @Column(name = "min_value", nullable = false)
    private Double minValue;

    @Column(name = "max_value", nullable = false)
    private Double maxValue;
}
