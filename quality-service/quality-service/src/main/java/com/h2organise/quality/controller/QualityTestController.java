package com.h2organise.quality.controller;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.service.QualityTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QualityTestController {

    private final QualityTestService service;

    // example: GET /tests?zoneId=1&status=NON_COMPLIANT
    @GetMapping("/tests")
    public List<QualityTestResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "sourceId", required = false) Long sourceId,
            @RequestParam(name = "status", required = false) ComplianceStatus status) {
        return service.findAll(zoneId, sourceId, status);
    }

    @GetMapping("/tests/latest")
    public QualityTestResponse latest(@RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.findLatest(zoneId);
    }

    @GetMapping("/tests/{id}")
    public QualityTestResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/tests")
    @ResponseStatus(HttpStatus.CREATED)
    public QualityTestResponse create(@Valid @RequestBody QualityTestRequest request) {
        return service.create(request);
    }

    @PutMapping("/tests/{id}")
    public QualityTestResponse update(@PathVariable("id") Long id, @Valid @RequestBody QualityTestRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/tests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    // example: GET /compliance/summary?zoneId=1
    @GetMapping("/compliance/summary")
    public ComplianceSummaryResponse summary(@RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.summary(zoneId);
    }
}
