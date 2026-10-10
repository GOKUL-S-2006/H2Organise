package com.h2organise.maintenance.controller;

import com.h2organise.maintenance.dto.FaultRequest;
import com.h2organise.maintenance.dto.FaultResponse;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;
import com.h2organise.maintenance.service.FaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faults")
@RequiredArgsConstructor
public class FaultController {

    private final FaultService service;

    // example: GET /faults?zoneId=1&status=OPEN&severity=HIGH
    @GetMapping
    public List<FaultResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) FaultStatus status,
            @RequestParam(name = "severity", required = false) FaultSeverity severity) {
        return service.findAll(zoneId, status, severity);
    }

    @GetMapping("/{id}")
    public FaultResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FaultResponse create(@Valid @RequestBody FaultRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public FaultResponse update(@PathVariable("id") Long id, @Valid @RequestBody FaultRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/resolve")
    public FaultResponse resolve(@PathVariable("id") Long id) {
        return service.resolve(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
