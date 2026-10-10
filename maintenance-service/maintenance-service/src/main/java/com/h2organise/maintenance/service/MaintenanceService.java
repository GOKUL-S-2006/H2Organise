package com.h2organise.maintenance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h2organise.maintenance.model.Asset;
import com.h2organise.maintenance.model.MaintenanceJob;
import com.h2organise.maintenance.repository.AssetRepository;
import com.h2organise.maintenance.repository.MaintenanceJobRepository;

// fault reported -> asset FAULTY
// job started    -> asset UNDER_REPAIR
// job done       -> asset WORKING again
@Service
public class MaintenanceService {

    private final MaintenanceJobRepository jobRepo;
    private final AssetRepository assetRepo;
    private final AssetService assetService;

    public MaintenanceService(MaintenanceJobRepository jobRepo, AssetRepository assetRepo, AssetService assetService) {
        this.jobRepo = jobRepo;
        this.assetRepo = assetRepo;
        this.assetService = assetService;
    }

    public List<MaintenanceJob> getAllJobs() {
        return jobRepo.findAll();
    }

    public List<MaintenanceJob> getPendingJobs() {
        return jobRepo.findByStatusNot("DONE");
    }

    public MaintenanceJob reportFault(Long assetId, MaintenanceJob job) {
        Asset asset = assetService.getAsset(assetId);

        job.setAsset(asset);
        job.setStatus("OPEN");
        job.setReportedDate(LocalDate.now());
        if (job.getPriority() == null) {
            job.setPriority("MEDIUM");
        }

        asset.setStatus("FAULTY");
        assetRepo.save(asset);
        return jobRepo.save(job);
    }

    public MaintenanceJob startJob(Long jobId, String assignedTo) {
        MaintenanceJob job = getJob(jobId);
        if (!job.getStatus().equals("OPEN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only OPEN jobs can be started");
        }
        job.setStatus("IN_PROGRESS");
        job.setAssignedTo(assignedTo);

        job.getAsset().setStatus("UNDER_REPAIR");
        assetRepo.save(job.getAsset());
        return jobRepo.save(job);
    }

    public MaintenanceJob completeJob(Long jobId, double cost, String notes) {
        MaintenanceJob job = getJob(jobId);
        if (job.getStatus().equals("DONE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job is already done");
        }
        job.setStatus("DONE");
        job.setCompletedDate(LocalDate.now());
        job.setCost(cost);
        job.setNotes(notes);

        Asset asset = job.getAsset();
        asset.setStatus("WORKING");
        asset.setLastServiced(LocalDate.now());
        assetRepo.save(asset);
        return jobRepo.save(job);
    }

    private MaintenanceJob getJob(Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    public Map<String, Object> getSummary() {
        double totalCost = 0;
        for (MaintenanceJob job : jobRepo.findAll()) {
            totalCost += job.getCost();
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAssets", assetRepo.count());
        summary.put("working", assetRepo.countByStatus("WORKING"));
        summary.put("faulty", assetRepo.countByStatus("FAULTY"));
        summary.put("underRepair", assetRepo.countByStatus("UNDER_REPAIR"));
        summary.put("pendingJobs", getPendingJobs().size());
        summary.put("totalCost", totalCost);
        return summary;
    }
}
