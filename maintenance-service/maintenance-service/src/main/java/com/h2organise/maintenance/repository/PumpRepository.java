package com.h2organise.maintenance.repository;

import com.h2organise.maintenance.entity.Pump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PumpRepository extends JpaRepository<Pump, Long>, JpaSpecificationExecutor<Pump> {}
