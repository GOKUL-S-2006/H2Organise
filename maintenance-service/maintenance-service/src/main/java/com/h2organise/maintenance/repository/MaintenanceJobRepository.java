package com.h2organise.maintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.maintenance.model.MaintenanceJob;

public interface MaintenanceJobRepository extends JpaRepository<MaintenanceJob, Long> {

    List<MaintenanceJob> findByStatusNot(String status);

    boolean existsByAssetId(Long assetId);
}
