package com.h2organise.quality.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.h2organise.quality.model.QualityLimit;
import com.h2organise.quality.model.QualityTest;
import com.h2organise.quality.service.QualityService;

@RestController
@RequestMapping("/api/quality")
public class QualityController {

    private final QualityService qualityService;

    public QualityController(QualityService qualityService) {
        this.qualityService = qualityService;
    }

    @GetMapping("/tests")
    public List<QualityTest> getTests() {
        return qualityService.getAllTests();
    }

    @GetMapping("/tests/failed")
    public List<QualityTest> getFailedTests() {
        return qualityService.getFailedTests();
    }

    @PostMapping("/tests")
    public QualityTest addTest(@RequestBody QualityTest test) {
        return qualityService.addTest(test);
    }

    @DeleteMapping("/tests/{id}")
    public void deleteTest(@PathVariable Long id) {
        qualityService.deleteTest(id);
    }

    @GetMapping("/limits")
    public List<QualityLimit> getLimits() {
        return qualityService.getLimits();
    }

    @PutMapping("/limits")
    public QualityLimit updateLimit(@RequestBody QualityLimit limit) {
        return qualityService.updateLimit(limit);
    }

    @GetMapping("/summary")
    public Map<String, Long> getSummary() {
        return qualityService.getSummary();
    }
}
