package com.h2organise.maintenance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.h2organise.maintenance.model.MaintenanceJob;
import com.h2organise.maintenance.service.MaintenanceService;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<MaintenanceJob> getJobs() {
        return maintenanceService.getAllJobs();
    }

    @GetMapping("/pending")
    public List<MaintenanceJob> getPendingJobs() {
        return maintenanceService.getPendingJobs();
    }

    // body: {"problem": "...", "priority": "HIGH", "reportedBy": "..."}
    @PostMapping("/report/{assetId}")
    public MaintenanceJob reportFault(@PathVariable Long assetId, @RequestBody MaintenanceJob job) {
        return maintenanceService.reportFault(assetId, job);
    }

    @PutMapping("/{id}/start")
    public MaintenanceJob startJob(@PathVariable Long id, @RequestParam String assignedTo) {
        return maintenanceService.startJob(id, assignedTo);
    }

    @PutMapping("/{id}/complete")
    public MaintenanceJob completeJob(@PathVariable Long id, @RequestParam double cost,
                                      @RequestParam(required = false) String notes) {
        return maintenanceService.completeJob(id, cost, notes);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return maintenanceService.getSummary();
    }
}
