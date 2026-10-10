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

import com.h2organise.maintenance.dto.FaultRequest;
import com.h2organise.maintenance.dto.FaultResponse;
import com.h2organise.maintenance.entity.FaultSeverity;
import com.h2organise.maintenance.entity.FaultStatus;
import com.h2organise.maintenance.service.FaultService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/faults")
public class FaultController {

    private final FaultService faultService;

    public FaultController(FaultService faultService) {
        this.faultService = faultService;
    }

    // GET /faults?zoneId=1&status=OPEN&severity=HIGH (all optional)
    @GetMapping
    public List<FaultResponse> getFaults(@RequestParam(required = false) Long zoneId,
                                      @RequestParam(required = false) FaultStatus status,
                                      @RequestParam(required = false) FaultSeverity severity) {
        return faultService.getFaults(zoneId, status, severity);
    }

    @GetMapping("/{id}")
    public FaultResponse getFault(@PathVariable Long id) {
        return faultService.getFault(id);
    }

    @PostMapping
    public ResponseEntity<FaultResponse> createFault(@Valid @RequestBody FaultRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faultService.createFault(request));
    }

    @PutMapping("/{id}")
    public FaultResponse updateFault(@PathVariable Long id, @Valid @RequestBody FaultRequest request) {
        return faultService.updateFault(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFault(@PathVariable Long id) {
        faultService.deleteFault(id);
        return ResponseEntity.noContent().build();
    }

    // marks the fault RESOLVED and sets resolvedAt
    @PutMapping("/{id}/resolve")
    public FaultResponse resolveFault(@PathVariable Long id) {
        return faultService.resolveFault(id);
    }
}
