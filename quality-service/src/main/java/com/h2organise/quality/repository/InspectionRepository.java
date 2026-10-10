package com.h2organise.quality.repository;

import com.h2organise.quality.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InspectionRepository
        extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {
}
