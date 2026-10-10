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

import com.h2organise.maintenance.dto.PumpRequest;
import com.h2organise.maintenance.dto.PumpResponse;
import com.h2organise.maintenance.entity.PumpStatus;
import com.h2organise.maintenance.service.PumpService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pumps")
public class PumpController {

    private final PumpService pumpService;

    public PumpController(PumpService pumpService) {
        this.pumpService = pumpService;
    }

    // GET /pumps?zoneId=1&status=FAULTY (both optional)
    @GetMapping
    public List<PumpResponse> getPumps(@RequestParam(required = false) Long zoneId,
                                     @RequestParam(required = false) PumpStatus status) {
        return pumpService.getPumps(zoneId, status);
    }

    @GetMapping("/{id}")
    public PumpResponse getPump(@PathVariable Long id) {
        return pumpService.getPump(id);
    }

    @PostMapping
    public ResponseEntity<PumpResponse> createPump(@Valid @RequestBody PumpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pumpService.createPump(request));
    }

    @PutMapping("/{id}")
    public PumpResponse updatePump(@PathVariable Long id, @Valid @RequestBody PumpRequest request) {
        return pumpService.updatePump(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePump(@PathVariable Long id) {
        pumpService.deletePump(id);
        return ResponseEntity.noContent().build();
    }
}
