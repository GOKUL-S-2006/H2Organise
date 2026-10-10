package com.h2organise.maintenance.service;

import com.h2organise.maintenance.dto.FaultRequest;
import com.h2organise.maintenance.dto.FaultResponse;
import com.h2organise.maintenance.entity.Fault;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;
import com.h2organise.maintenance.entity.MaintenanceTask;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.FaultRepository;
import com.h2organise.maintenance.repository.MaintenanceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaultService {

    private final FaultRepository repository;
    private final MaintenanceTaskRepository taskRepository;

    // zoneId, status and severity are all optional filters
    public List<FaultResponse> findAll(Long zoneId, FaultStatus status, FaultSeverity severity) {
        Specification<Fault> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (severity != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("severity"), severity));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(FaultResponse::from).toList();
    }

    public FaultResponse findById(Long id) {
        return FaultResponse.from(getEntity(id));
    }

    private Fault getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fault not found with id " + id));
    }

    @Transactional
    public FaultResponse create(FaultRequest request) {
        Fault fault = new Fault();
        fault.setReportedAt(LocalDateTime.now());
        apply(fault, request);
        return FaultResponse.from(repository.save(fault));
    }

    @Transactional
    public FaultResponse update(Long id, FaultRequest request) {
        Fault fault = getEntity(id);
        apply(fault, request);
        return FaultResponse.from(repository.save(fault));
    }

    @Transactional
    public FaultResponse resolve(Long id) {
        Fault fault = getEntity(id);
        setStatus(fault, FaultStatus.RESOLVED);
        return FaultResponse.from(repository.save(fault));
    }

    @Transactional
    public void delete(Long id) {
        Fault fault = getEntity(id);
        // keep the tasks, just unlink them from the deleted fault
        for (MaintenanceTask task : taskRepository.findByFaultId(id)) {
            task.setFault(null);
        }
        repository.delete(fault);
    }

    private void apply(Fault f, FaultRequest r) {
        f.setAssetType(r.assetType());
        f.setAssetId(r.assetId());
        f.setZoneId(r.zoneId());
        f.setDescription(r.description());
        f.setSeverity(r.severity());
        f.setReportedBy(r.reportedBy());
        if (r.status() != null) {
            setStatus(f, r.status());
        }
    }

    // resolvedAt is filled when the fault is resolved and cleared if it is opened again
    private void setStatus(Fault fault, FaultStatus status) {
        fault.setStatus(status);
        if (status != FaultStatus.RESOLVED) {
            fault.setResolvedAt(null);
        } else if (fault.getResolvedAt() == null) {
            fault.setResolvedAt(LocalDateTime.now());
        }
    }
}
