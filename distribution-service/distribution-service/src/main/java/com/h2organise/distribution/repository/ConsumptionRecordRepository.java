package com.h2organise.distribution.repository;

import com.h2organise.distribution.dto.CategorySummary;
import com.h2organise.distribution.entity.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumptionRecordRepository
        extends JpaRepository<ConsumptionRecord, Long>, JpaSpecificationExecutor<ConsumptionRecord> {

    // total water used per category for ALL zones
    @Query("select new com.h2organise.distribution.dto.CategorySummary(c.category, sum(c.volumeConsumed)) "
            + "from ConsumptionRecord c group by c.category")
    List<CategorySummary> summarizeAll();

    // same thing but only for one zone
    @Query("select new com.h2organise.distribution.dto.CategorySummary(c.category, sum(c.volumeConsumed)) "
            + "from ConsumptionRecord c where c.zone.id = :zoneId group by c.category")
    List<CategorySummary> summarizeByZone(@Param("zoneId") Long zoneId);
}
