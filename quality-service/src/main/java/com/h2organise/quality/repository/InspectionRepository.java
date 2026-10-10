package com.h2organise.quality.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.quality.entity.Inspection;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {

    List<Inspection> findByNextDueDateBetweenOrderByNextDueDateAsc(LocalDate from, LocalDate to);
}
