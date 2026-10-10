package com.h2organise.quality.repository;

import com.h2organise.quality.entity.QualityParameter;
import com.h2organise.quality.entity.QualityStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QualityStandardRepository extends JpaRepository<QualityStandard, Long> {

    Optional<QualityStandard> findByParameter(QualityParameter parameter);
}
