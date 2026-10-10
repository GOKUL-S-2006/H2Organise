package com.h2organise.quality.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h2organise.quality.model.QualityLimit;
import com.h2organise.quality.model.QualityTest;
import com.h2organise.quality.repository.QualityLimitRepository;
import com.h2organise.quality.repository.QualityTestRepository;

@Service
public class QualityService {

    private final QualityTestRepository testRepo;
    private final QualityLimitRepository limitRepo;

    public QualityService(QualityTestRepository testRepo, QualityLimitRepository limitRepo) {
        this.testRepo = testRepo;
        this.limitRepo = limitRepo;
    }

    public List<QualityTest> getAllTests() {
        return testRepo.findAll();
    }

    public List<QualityTest> getFailedTests() {
        return testRepo.findByResult("FAIL");
    }

    public QualityTest addTest(QualityTest test) {
        if (test.getWaterUse() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "waterUse is required");
        }
        if (test.getTestDate() == null) {
            test.setTestDate(LocalDate.now());
        }

        QualityLimit limit = limitRepo.findById(test.getWaterUse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No limits set for " + test.getWaterUse()));

        // check each value against the limit
        String reason = "";
        if (test.getPh() < limit.getPhMin() || test.getPh() > limit.getPhMax()) {
            reason += "pH out of range. ";
        }
        if (test.getTurbidity() > limit.getTurbidityMax()) {
            reason += "Turbidity too high. ";
        }
        if (test.getTds() > limit.getTdsMax()) {
            reason += "TDS too high. ";
        }

        if (reason.isEmpty()) {
            test.setResult("PASS");
            test.setReason(null);
        } else {
            test.setResult("FAIL");
            test.setReason(reason.trim());
        }
        return testRepo.save(test);
    }

    public void deleteTest(Long id) {
        if (!testRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Test not found");
        }
        testRepo.deleteById(id);
    }

    public List<QualityLimit> getLimits() {
        return limitRepo.findAll();
    }

    public QualityLimit updateLimit(QualityLimit limit) {
        if (limit.getPhMin() >= limit.getPhMax()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phMin must be less than phMax");
        }
        return limitRepo.save(limit);
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("total", testRepo.count());
        summary.put("passed", testRepo.countByResult("PASS"));
        summary.put("failed", testRepo.countByResult("FAIL"));
        return summary;
    }
}
