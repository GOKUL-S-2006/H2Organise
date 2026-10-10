package com.h2organise.maintenance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h2organise.maintenance.dto.CompleteTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskResponse;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.service.MaintenanceTaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class MaintenanceTaskController {

    private final MaintenanceTaskService taskService;

    public MaintenanceTaskController(MaintenanceTaskService taskService) {
        this.taskService = taskService;
    }

    // GET /tasks?zoneId=1&status=PENDING&assignedTo=Suresh (all optional)
    @GetMapping
    public List<MaintenanceTaskResponse> getTasks(@RequestParam(required = false) Long zoneId,
                                                @RequestParam(required = false) TaskStatus status,
                                                @RequestParam(required = false) String assignedTo) {
        return taskService.getTasks(zoneId, status, assignedTo);
    }

    @GetMapping("/{id}")
    public MaintenanceTaskResponse getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public ResponseEntity<MaintenanceTaskResponse> createTask(@Valid @RequestBody MaintenanceTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PutMapping("/{id}")
    public MaintenanceTaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody MaintenanceTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // marks the task COMPLETED and sets completedDate; body { "cost", "remarks" } is optional
    @PutMapping("/{id}/complete")
    public MaintenanceTaskResponse completeTask(@PathVariable Long id,
                                            @Valid @RequestBody(required = false) CompleteTaskRequest request) {
        return taskService.completeTask(id, request);
    }
}
