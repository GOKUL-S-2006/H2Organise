package com.h2organise.complaint.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.h2organise.complaint.model.Complaint;
import com.h2organise.complaint.service.ComplaintService;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // ?status=OPEN is optional
    @GetMapping
    public List<Complaint> getComplaints(@RequestParam(required = false) String status) {
        if (status != null) {
            return complaintService.getByStatus(status);
        }
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaint(@PathVariable Long id) {
        return complaintService.getComplaint(id);
    }

    // public user tracks with the number they got, e.g. /api/complaints/track/CMP-1001
    @GetMapping("/track/{complaintNumber}")
    public Complaint trackComplaint(@PathVariable String complaintNumber) {
        return complaintService.trackComplaint(complaintNumber);
    }

    @PostMapping
    public Complaint addComplaint(@RequestBody Complaint complaint) {
        return complaintService.addComplaint(complaint);
    }

    @PutMapping("/{id}/assign")
    public Complaint assignComplaint(@PathVariable Long id, @RequestParam String assignedTo) {
        return complaintService.assignComplaint(id, assignedTo);
    }

    @PutMapping("/{id}/resolve")
    public Complaint resolveComplaint(@PathVariable Long id, @RequestParam String remarks) {
        return complaintService.resolveComplaint(id, remarks);
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }

    @GetMapping("/summary")
    public Map<String, Long> getSummary() {
        return complaintService.getSummary();
    }
}
