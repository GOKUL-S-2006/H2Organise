package com.h2organise.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.quality.model.QualityTest;

public interface QualityTestRepository extends JpaRepository<QualityTest, Long> {

    List<QualityTest> findByResult(String result);

    long countByResult(String result);
}
