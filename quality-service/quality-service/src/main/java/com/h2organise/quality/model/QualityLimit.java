package com.h2organise.quality.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// allowed limits for each type of water use (one row per use)
@Entity
@Table(name = "quality_limits")
@Getter
@Setter
public class QualityLimit {

    @Id
    @Enumerated(EnumType.STRING)
    private WaterUse waterUse;

    private double phMin;
    private double phMax;
    private double turbidityMax; // NTU
    private double tdsMax;       // mg/L
}
