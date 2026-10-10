package com.h2organise.maintenance.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.maintenance.dto.FaultRequest;
import com.h2organise.maintenance.dto.FaultResponse;
import com.h2organise.maintenance.entity.Fault;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;
import com.h2organise.maintenance.entity.MaintenanceTask;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.FaultRepository;
import com.h2organise.maintenance.repository.MaintenanceTaskRepository;

@Service
public class FaultService {

    private final FaultRepository faultRepository;
    private final MaintenanceTaskRepository taskRepository;

    public FaultService(FaultRepository faultRepository, MaintenanceTaskRepository taskRepository) {
        this.faultRepository = faultRepository;
        this.taskRepository = taskRepository;
    }

    // All filters are optional and are combined with AND
    @Transactional(readOnly = true)
    public List<FaultResponse> getFaults(Long zoneId, FaultStatus status, FaultSeverity severity) {
        Fault probe = new Fault();
        probe.setZoneId(zoneId);
        probe.setStatus(status);
        probe.setSeverity(severity);

        List<FaultResponse> responses = new ArrayList<>();
        for (Fault fault : faultRepository.findAll(Example.of(probe), Sort.by("id"))) {
            responses.add(toResponse(fault));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public FaultResponse getFault(Long id) {
        return toResponse(findFault(id));
    }

    @Transactional
    public FaultResponse createFault(FaultRequest request) {
        Fault fault = new Fault();
        copyDetails(request, fault);
        fault.setReportedAt(now());
        changeStatus(fault, request.getStatus() != null ? request.getStatus() : FaultStatus.OPEN);
        return toResponse(faultRepository.save(fault));
    }

    // Updates details, and the status too when one is sent
    @Transactional
    public FaultResponse updateFault(Long id, FaultRequest request) {
        Fault fault = findFault(id);
        copyDetails(request, fault);
        if (request.getStatus() != null) {
            changeStatus(fault, request.getStatus());
        }
        return toResponse(faultRepository.save(fault));
    }

    @Transactional
    public FaultResponse resolveFault(Long id) {
        Fault fault = findFault(id);
        changeStatus(fault, FaultStatus.RESOLVED);
        return toResponse(faultRepository.save(fault));
    }

    @Transactional
    public void deleteFault(Long id) {
        Fault fault = findFault(id);
        // tasks keep their history but are no longer linked to the deleted fault
        for (MaintenanceTask task : taskRepository.findByFaultId(id)) {
            task.setFault(null);
        }
        faultRepository.delete(fault);
    }

    // ---------- helpers ----------

    // resolvedAt is set when a fault becomes RESOLVED and cleared if it is reopened
    private void changeStatus(Fault fault, FaultStatus newStatus) {
        fault.setStatus(newStatus);
        if (newStatus == FaultStatus.RESOLVED) {
            if (fault.getResolvedAt() == null) {
                fault.setResolvedAt(now());
            }
        } else {
            fault.setResolvedAt(null);
        }
    }

    private Fault findFault(Long id) {
        return faultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fault not found with id " + id));
    }

    private void copyDetails(FaultRequest request, Fault fault) {
        fault.setAssetType(request.getAssetType());
        fault.setAssetId(request.getAssetId());
        fault.setZoneId(request.getZoneId());
        fault.setDescription(request.getDescription());
        fault.setSeverity(request.getSeverity());
        fault.setReportedBy(request.getReportedBy());
    }

    private FaultResponse toResponse(Fault fault) {
        FaultResponse response = new FaultResponse();
        response.setId(fault.getId());
        response.setAssetType(fault.getAssetType());
        response.setAssetId(fault.getAssetId());
        response.setZoneId(fault.getZoneId());
        response.setDescription(fault.getDescription());
        response.setSeverity(fault.getSeverity());
        response.setStatus(fault.getStatus());
        response.setReportedAt(fault.getReportedAt());
        response.setResolvedAt(fault.getResolvedAt());
        response.setReportedBy(fault.getReportedBy());
        return response;
    }

    private LocalDateTime now() {
        return LocalDateTime.now().withNano(0);
    }
}
