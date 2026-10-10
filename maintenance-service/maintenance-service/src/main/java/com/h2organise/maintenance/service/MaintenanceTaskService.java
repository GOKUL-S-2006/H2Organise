package com.h2organise.maintenance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.maintenance.dto.CompleteTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskResponse;
import com.h2organise.maintenance.entity.Fault;
import com.h2organise.maintenance.entity.MaintenanceTask;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.FaultRepository;
import com.h2organise.maintenance.repository.MaintenanceTaskRepository;

@Service
public class MaintenanceTaskService {

    private final MaintenanceTaskRepository taskRepository;
    private final FaultRepository faultRepository;

    public MaintenanceTaskService(MaintenanceTaskRepository taskRepository, FaultRepository faultRepository) {
        this.taskRepository = taskRepository;
        this.faultRepository = faultRepository;
    }

    // All filters are optional and are combined with AND
    @Transactional(readOnly = true)
    public List<MaintenanceTaskResponse> getTasks(Long zoneId, TaskStatus status, String assignedTo) {
        MaintenanceTask probe = new MaintenanceTask();
        probe.setZoneId(zoneId);
        probe.setStatus(status);
        probe.setAssignedTo(assignedTo);

        List<MaintenanceTaskResponse> responses = new ArrayList<>();
        for (MaintenanceTask task : taskRepository.findAll(Example.of(probe), Sort.by("id"))) {
            responses.add(toResponse(task));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public MaintenanceTaskResponse getTask(Long id) {
        return toResponse(findTask(id));
    }

    @Transactional
    public MaintenanceTaskResponse createTask(MaintenanceTaskRequest request) {
        MaintenanceTask task = new MaintenanceTask();
        copyDetails(request, task);
        changeStatus(task, request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public MaintenanceTaskResponse updateTask(Long id, MaintenanceTaskRequest request) {
        MaintenanceTask task = findTask(id);
        copyDetails(request, task);
        if (request.getStatus() != null) {
            changeStatus(task, request.getStatus());
        }
        return toResponse(taskRepository.save(task));
    }

    // Marks the task COMPLETED. cost and remarks are optional.
    @Transactional
    public MaintenanceTaskResponse completeTask(Long id, CompleteTaskRequest request) {
        MaintenanceTask task = findTask(id);
        changeStatus(task, TaskStatus.COMPLETED);
        if (request != null && request.getCost() != null) {
            task.setCost(request.getCost());
        }
        if (request != null && request.getRemarks() != null) {
            task.setRemarks(request.getRemarks());
        }
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.delete(findTask(id));
    }

    // ---------- helpers ----------

    // completedDate is set when a task becomes COMPLETED and cleared if it is reopened
    private void changeStatus(MaintenanceTask task, TaskStatus newStatus) {
        task.setStatus(newStatus);
        if (newStatus == TaskStatus.COMPLETED) {
            if (task.getCompletedDate() == null) {
                task.setCompletedDate(LocalDate.now());
            }
        } else {
            task.setCompletedDate(null);
        }
    }

    private MaintenanceTask findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    private void copyDetails(MaintenanceTaskRequest request, MaintenanceTask task) {
        Fault fault = null;
        if (request.getFaultId() != null) {
            fault = faultRepository.findById(request.getFaultId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fault not found with id " + request.getFaultId()));
        }
        task.setFault(fault);
        task.setAssetType(request.getAssetType());
        task.setAssetId(request.getAssetId());
        task.setZoneId(request.getZoneId());
        task.setTitle(request.getTitle().trim());
        task.setTaskType(request.getTaskType());
        task.setAssignedTo(request.getAssignedTo());
        task.setScheduledDate(request.getScheduledDate());
        task.setCost(request.getCost());
        task.setRemarks(request.getRemarks());
    }

    private MaintenanceTaskResponse toResponse(MaintenanceTask task) {
        MaintenanceTaskResponse response = new MaintenanceTaskResponse();
        response.setId(task.getId());
        if (task.getFault() != null) {
            response.setFaultId(task.getFault().getId());
        }
        response.setAssetType(task.getAssetType());
        response.setAssetId(task.getAssetId());
        response.setZoneId(task.getZoneId());
        response.setTitle(task.getTitle());
        response.setTaskType(task.getTaskType());
        response.setAssignedTo(task.getAssignedTo());
        response.setScheduledDate(task.getScheduledDate());
        response.setCompletedDate(task.getCompletedDate());
        response.setStatus(task.getStatus());
        response.setCost(task.getCost());
        response.setRemarks(task.getRemarks());
        return response;
    }
}
