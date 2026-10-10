package com.h2organise.maintenance.controller;

import com.h2organise.maintenance.dto.CompleteTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskRequest;
import com.h2organise.maintenance.dto.MaintenanceTaskResponse;
import com.h2organise.maintenance.entity.TaskStatus;
import com.h2organise.maintenance.service.MaintenanceTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class MaintenanceTaskController {

    private final MaintenanceTaskService service;

    // example: GET /tasks?zoneId=1&status=PENDING&assignedTo=Suresh
    @GetMapping
    public List<MaintenanceTaskResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "assignedTo", required = false) String assignedTo) {
        return service.findAll(zoneId, status, assignedTo);
    }

    @GetMapping("/{id}")
    public MaintenanceTaskResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceTaskResponse create(@Valid @RequestBody MaintenanceTaskRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MaintenanceTaskResponse update(@PathVariable("id") Long id, @Valid @RequestBody MaintenanceTaskRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/complete")
    public MaintenanceTaskResponse complete(@PathVariable("id") Long id,
                                            @Valid @RequestBody(required = false) CompleteTaskRequest request) {
        return service.complete(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
