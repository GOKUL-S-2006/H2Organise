package com.h2organise.maintenance.repository;

import com.h2organise.maintenance.entity.Fault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FaultRepository extends JpaRepository<Fault, Long>, JpaSpecificationExecutor<Fault> {}
