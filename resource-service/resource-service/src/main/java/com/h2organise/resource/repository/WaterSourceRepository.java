package com.h2organise.resource.repository;

import com.h2organise.resource.entity.WaterSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// JpaSpecificationExecutor lets us add optional filters (zoneId, type, status) in the service
public interface WaterSourceRepository extends JpaRepository<WaterSource, Long>, JpaSpecificationExecutor<WaterSource> {
}
