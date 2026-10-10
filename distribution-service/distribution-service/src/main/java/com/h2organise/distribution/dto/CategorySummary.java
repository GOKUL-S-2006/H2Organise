package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.ConsumptionCategory;

// used by /consumption/summary, e.g. DOMESTIC -> 38.0
public record CategorySummary(ConsumptionCategory category, Double totalVolume) {
}
