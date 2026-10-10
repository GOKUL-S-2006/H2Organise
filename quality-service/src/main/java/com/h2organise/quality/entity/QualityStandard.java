package com.h2organise.quality.entity;

import jakarta.persistence.*;
import lombok.*;

/** Acceptable range for one water-quality parameter (for example pH 6.5 to 8.5). */
@Entity
@Table(name = "quality_standards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private Parameter parameter;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    public enum Parameter {
        PH,
        TURBIDITY,
        CHLORINE,
        TDS
    }
}
