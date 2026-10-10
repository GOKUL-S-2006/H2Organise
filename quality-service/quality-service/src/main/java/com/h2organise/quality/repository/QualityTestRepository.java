package com.h2organise.quality.repository;

import com.h2organise.quality.entity.QualityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface QualityTestRepository extends JpaRepository<QualityTest, Long>, JpaSpecificationExecutor<QualityTest> {

    List<QualityTest> findByZoneId(Long zoneId);

    Optional<QualityTest> findFirstByOrderByTestDateDescIdDesc();

    Optional<QualityTest> findFirstByZoneIdOrderByTestDateDescIdDesc(Long zoneId);
}
