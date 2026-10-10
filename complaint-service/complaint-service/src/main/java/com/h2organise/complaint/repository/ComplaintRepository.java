package com.h2organise.complaint.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h2organise.complaint.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Optional<Complaint> findByComplaintNumber(String complaintNumber);

    List<Complaint> findByStatus(String status);

    long countByStatus(String status);
}
