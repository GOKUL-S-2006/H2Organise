package com.h2organise.quality.controller;

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

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.InspectionResult;
import com.h2organise.quality.service.InspectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inspections")
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    // GET /inspections?zoneId=1&result=PASSED (all optional)
    @GetMapping
    public List<InspectionResponse> getInspections(@RequestParam(required = false) Long zoneId,
                                                   @RequestParam(required = false) InspectionResult result) {
        return inspectionService.getInspections(zoneId, result);
    }

    @GetMapping("/due")
    public List<InspectionResponse> getDueInspections() {
        return inspectionService.getDueInspections();
    }

    @GetMapping("/{id}")
    public InspectionResponse getInspection(@PathVariable Long id) {
        return inspectionService.getInspection(id);
    }

    @PostMapping
    public ResponseEntity<InspectionResponse> createInspection(@Valid @RequestBody InspectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionService.createInspection(request));
    }

    @PutMapping("/{id}")
    public InspectionResponse updateInspection(@PathVariable Long id, @Valid @RequestBody InspectionRequest request) {
        return inspectionService.updateInspection(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInspection(@PathVariable Long id) {
        inspectionService.deleteInspection(id);
        return ResponseEntity.noContent().build();
    }
}
