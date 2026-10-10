package com.h2organise.quality.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.quality.entity.QualityParameter;
import com.h2organise.quality.entity.QualityStandard;

public interface QualityStandardRepository extends JpaRepository<QualityStandard, Long> {

    Optional<QualityStandard> findByParameter(QualityParameter parameter);
}
