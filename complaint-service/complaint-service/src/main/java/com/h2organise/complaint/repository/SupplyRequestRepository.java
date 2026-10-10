package com.h2organise.complaint.repository;

import com.h2organise.complaint.entity.SupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplyRequestRepository extends JpaRepository<SupplyRequest, Long>, JpaSpecificationExecutor<SupplyRequest> {
}
