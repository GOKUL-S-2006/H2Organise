package com.h2organise.distribution.repository;

import com.h2organise.distribution.entity.DistributionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DistributionScheduleRepository
        extends JpaRepository<DistributionSchedule, Long>, JpaSpecificationExecutor<DistributionSchedule> {
}
