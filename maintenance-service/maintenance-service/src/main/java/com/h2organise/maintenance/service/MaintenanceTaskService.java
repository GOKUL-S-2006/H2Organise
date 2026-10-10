package com.h2organise.maintenance.service;

import com.h2organise.maintenance.dto.CompleteTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskResponse;
import com.h2organise.maintenance.entity.MaintenanceTask;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.FaultRepository;
import com.h2organise.maintenance.repository.MaintenanceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceTaskService {

    private final MaintenanceTaskRepository repository;
    private final FaultRepository faultRepository;

    // zoneId, status and assignedTo are all optional filters
    public List<MaintenanceTaskResponse> findAll(Long zoneId, TaskStatus status, String assignedTo) {
        Specification<MaintenanceTask> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (assignedTo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("assignedTo"), assignedTo));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(MaintenanceTaskResponse::from).toList();
    }

    public MaintenanceTaskResponse findById(Long id) {
        return MaintenanceTaskResponse.from(getEntity(id));
    }

    private MaintenanceTask getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    @Transactional
    public MaintenanceTaskResponse create(MaintenanceTaskRequest request) {
        MaintenanceTask task = new MaintenanceTask();
        apply(task, request);
        return MaintenanceTaskResponse.from(repository.save(task));
    }

    @Transactional
    public MaintenanceTaskResponse update(Long id, MaintenanceTaskRequest request) {
        MaintenanceTask task = getEntity(id);
        apply(task, request);
        return MaintenanceTaskResponse.from(repository.save(task));
    }

    // body is optional, it can carry the final cost and remarks
    @Transactional
    public MaintenanceTaskResponse complete(Long id, CompleteTaskRequest request) {
        MaintenanceTask task = getEntity(id);
        setStatus(task, TaskStatus.COMPLETED);
        if (request != null && request.cost() != null) {
            task.setCost(request.cost());
        }
        if (request != null && request.remarks() != null) {
            task.setRemarks(request.remarks());
        }
        return MaintenanceTaskResponse.from(repository.save(task));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private void apply(MaintenanceTask t, MaintenanceTaskRequest r) {
        if (r.faultId() == null) {
            t.setFault(null);
        } else {
            t.setFault(faultRepository.findById(r.faultId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fault not found with id " + r.faultId())));
        }
        t.setAssetType(r.assetType());
        t.setAssetId(r.assetId());
        t.setZoneId(r.zoneId());
        t.setTitle(r.title());
        t.setTaskType(r.taskType());
        t.setAssignedTo(r.assignedTo());
        t.setScheduledDate(r.scheduledDate());
        t.setCost(r.cost());
        t.setRemarks(r.remarks());
        if (r.status() != null) {
            setStatus(t, r.status());
        }
    }

    // completedDate is filled when the task is completed and cleared if it is reopened
    private void setStatus(MaintenanceTask task, TaskStatus status) {
        task.setStatus(status);
        if (status != TaskStatus.COMPLETED) {
            task.setCompletedDate(null);
        } else if (task.getCompletedDate() == null) {
            task.setCompletedDate(LocalDate.now());
        }
    }
}
