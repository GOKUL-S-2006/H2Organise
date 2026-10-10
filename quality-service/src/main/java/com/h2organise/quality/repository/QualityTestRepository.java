package com.h2organise.quality.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.entity.QualityTest;

public interface QualityTestRepository extends JpaRepository<QualityTest, Long> {

    Optional<QualityTest> findFirstByOrderByTestDateDescIdDesc();

    Optional<QualityTest> findFirstByZoneIdOrderByTestDateDescIdDesc(Long zoneId);

    List<QualityTest> findByZoneId(Long zoneId);

    long countByComplianceStatus(ComplianceStatus status);

    long countByZoneIdAndComplianceStatus(Long zoneId, ComplianceStatus status);

    long countByZoneId(Long zoneId);
}
