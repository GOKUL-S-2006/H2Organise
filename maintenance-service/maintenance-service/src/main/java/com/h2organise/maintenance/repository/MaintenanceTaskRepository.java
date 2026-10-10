package com.h2organise.maintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.maintenance.entity.MaintenanceTask;

public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {

    List<MaintenanceTask> findByFaultId(Long faultId);
}
