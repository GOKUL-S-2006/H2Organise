package com.h2organise.maintenance.repository;

import com.h2organise.maintenance.entity.TreatmentPlant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentPlantRepository extends JpaRepository<TreatmentPlant, Long> {

    List<TreatmentPlant> findByZoneId(Long zoneId, Sort sort);
}
