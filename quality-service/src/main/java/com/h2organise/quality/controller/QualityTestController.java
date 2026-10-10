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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.service.QualityTestService;

import jakarta.validation.Valid;

@RestController
public class QualityTestController {

    private final QualityTestService testService;

    public QualityTestController(QualityTestService testService) {
        this.testService = testService;
    }

    // GET /tests?zoneId=1&sourceId=1&status=COMPLIANT (all optional)
    @GetMapping("/tests")
    public List<QualityTestResponse> getTests(@RequestParam(required = false) Long zoneId,
                                              @RequestParam(required = false) Long sourceId,
                                              @RequestParam(required = false) ComplianceStatus status) {
        return testService.getTests(zoneId, sourceId, status);
    }

    @GetMapping("/tests/latest")
    public QualityTestResponse getLatestTest(@RequestParam(required = false) Long zoneId) {
        return testService.getLatestTest(zoneId);
    }

    @GetMapping("/tests/{id}")
    public QualityTestResponse getTest(@PathVariable Long id) {
        return testService.getTest(id);
    }

    @PostMapping("/tests")
    public ResponseEntity<QualityTestResponse> createTest(@Valid @RequestBody QualityTestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTest(request));
    }

    @PutMapping("/tests/{id}")
    public QualityTestResponse updateTest(@PathVariable Long id, @Valid @RequestBody QualityTestRequest request) {
        return testService.updateTest(id, request);
    }

    @DeleteMapping("/tests/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    // GET /compliance/summary or /compliance/summary?zoneId=1
    @GetMapping("/compliance/summary")
    public ComplianceSummaryResponse getComplianceSummary(@RequestParam(required = false) Long zoneId) {
        return testService.getComplianceSummary(zoneId);
    }
}
