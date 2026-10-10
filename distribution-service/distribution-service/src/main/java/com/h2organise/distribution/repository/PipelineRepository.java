package com.h2organise.distribution.repository;

import com.h2organise.distribution.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// JpaSpecificationExecutor lets us add optional filters (zoneId, status) in the service
public interface PipelineRepository extends JpaRepository<Pipeline, Long>, JpaSpecificationExecutor<Pipeline> {
}
