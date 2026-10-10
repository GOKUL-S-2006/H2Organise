package com.h2organise.distribution.repository;

import com.h2organise.distribution.entity.SupplyZone;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository gives us save, findAll, findById, delete without writing any sql
public interface SupplyZoneRepository extends JpaRepository<SupplyZone, Long> {
}
