package com.h2organise.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.quality.model.QualityLimit;
import com.h2organise.quality.model.WaterUse;

public interface QualityLimitRepository extends JpaRepository<QualityLimit, WaterUse> {
}
