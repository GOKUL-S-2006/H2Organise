package com.h2organise.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.maintenance.entity.Pump;

public interface PumpRepository extends JpaRepository<Pump, Long> {
}
