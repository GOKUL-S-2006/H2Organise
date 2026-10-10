package com.h2organise.quality.repository;

import com.h2organise.quality.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface InspectionRepository extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {

    List<Inspection> findByNextDueDateBetweenOrderByNextDueDate(LocalDate from, LocalDate to);
}
