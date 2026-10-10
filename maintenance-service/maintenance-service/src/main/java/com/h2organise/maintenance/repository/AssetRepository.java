package com.h2organise.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.maintenance.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    long countByStatus(String status);
}
