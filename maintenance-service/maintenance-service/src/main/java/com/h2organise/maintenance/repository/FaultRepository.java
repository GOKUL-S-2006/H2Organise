package com.h2organise.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.maintenance.entity.Fault;

public interface FaultRepository extends JpaRepository<Fault, Long> {
}
