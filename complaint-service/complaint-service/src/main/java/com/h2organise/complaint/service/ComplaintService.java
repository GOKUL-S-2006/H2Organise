package com.h2organise.complaint.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h2organise.complaint.model.Complaint;
import com.h2organise.complaint.repository.ComplaintRepository;

// OPEN -> IN_PROGRESS (staff assigned) -> RESOLVED
@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepo;

    public ComplaintService(ComplaintRepository complaintRepo) {
        this.complaintRepo = complaintRepo;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepo.findAll();
    }

    public List<Complaint> getByStatus(String status) {
        return complaintRepo.findByStatus(status.toUpperCase());
    }

    public Complaint getComplaint(Long id) {
        return complaintRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
    }

    public Complaint trackComplaint(String complaintNumber) {
        return complaintRepo.findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No complaint with number " + complaintNumber));
    }

    public Complaint addComplaint(Complaint complaint) {
        if (complaint.getDescription() == null || complaint.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (complaint.getCategory() == null) {
            complaint.setCategory("OTHER");
        }

        complaint.setPriority(getPriority(complaint.getCategory()));
        complaint.setStatus("OPEN");
        complaint.setCreatedDate(LocalDate.now());

        // save first so we get the id, then build the number from it
        Complaint saved = complaintRepo.save(complaint);
        saved.setComplaintNumber("CMP-" + (1000 + saved.getId()));
        return complaintRepo.save(saved);
    }

    // shortage and dirty water affect people the most, so they come first
    private String getPriority(String category) {
        if (category.equals("WATER_SHORTAGE") || category.equals("DIRTY_WATER")) {
            return "HIGH";
        }
        if (category.equals("LEAKAGE")) {
            return "MEDIUM";
        }
        return "LOW";
    }

    public Complaint assignComplaint(Long id, String assignedTo) {
        Complaint complaint = getComplaint(id);
        if (!complaint.getStatus().equals("OPEN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only OPEN complaints can be assigned");
        }
        complaint.setAssignedTo(assignedTo);
        complaint.setStatus("IN_PROGRESS");
        return complaintRepo.save(complaint);
    }

    public Complaint resolveComplaint(Long id, String remarks) {
        Complaint complaint = getComplaint(id);
        if (complaint.getStatus().equals("RESOLVED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint is already resolved");
        }
        complaint.setStatus("RESOLVED");
        complaint.setRemarks(remarks);
        complaint.setResolvedDate(LocalDate.now());
        return complaintRepo.save(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintRepo.delete(getComplaint(id));
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("total", complaintRepo.count());
        summary.put("open", complaintRepo.countByStatus("OPEN"));
        summary.put("inProgress", complaintRepo.countByStatus("IN_PROGRESS"));
        summary.put("resolved", complaintRepo.countByStatus("RESOLVED"));
        return summary;
    }
}
