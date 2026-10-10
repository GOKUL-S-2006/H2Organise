package com.h2organise.quality.repository;

import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface QualityTestRepository
        extends JpaRepository<QualityTest, Long>, JpaSpecificationExecutor<QualityTest> {

    Optional<QualityTest> findFirstByOrderByTestDateDescIdDesc();

    Optional<QualityTest> findFirstByZoneIdOrderByTestDateDescIdDesc(Long zoneId);

    long countByComplianceStatus(ComplianceStatus status);

    long countByZoneId(Long zoneId);

    long countByZoneIdAndComplianceStatus(Long zoneId, ComplianceStatus status);
}
