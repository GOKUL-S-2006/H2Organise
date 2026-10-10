package com.h2organise.maintenance.repository;

import com.h2organise.maintenance.entity.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long>, JpaSpecificationExecutor<MaintenanceTask> {

    List<MaintenanceTask> findByFaultId(Long faultId);
}
