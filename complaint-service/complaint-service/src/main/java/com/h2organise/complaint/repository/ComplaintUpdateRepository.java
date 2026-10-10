package com.h2organise.complaint.repository;

import com.h2organise.complaint.entity.ComplaintUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintUpdateRepository extends JpaRepository<ComplaintUpdate, Long> {

    // history of one complaint, oldest first (the "_" means complaint.id)
    List<ComplaintUpdate> findByComplaint_IdOrderByUpdatedAtAscIdAsc(Long complaintId);

    // used when a complaint is deleted, its history has to go first
    void deleteByComplaint_Id(Long complaintId);
}
