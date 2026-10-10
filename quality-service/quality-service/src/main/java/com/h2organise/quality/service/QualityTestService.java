package com.h2organise.quality.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.QualityStandardRepository;
import com.h2organise.quality.repository.QualityTestRepository;

@Service
public class QualityTestService {

    private final QualityTestRepository testRepository;
    private final QualityStandardRepository standardRepository;

    public QualityTestService(QualityTestRepository testRepository, QualityStandardRepository standardRepository) {
        this.testRepository = testRepository;
        this.standardRepository = standardRepository;
    }

    // Every filter is optional. Filters that are given are combined with AND.
    @Transactional(readOnly = true)
    public List<QualityTestResponse> getTests(Long zoneId, Long sourceId, ComplianceStatus status) {
        QualityTest probe = new QualityTest();
        probe.setZoneId(zoneId);
        probe.setSourceId(sourceId);
        probe.setComplianceStatus(status);

        List<QualityTestResponse> responses = new ArrayList<>();
        for (QualityTest test : testRepository.findAll(Example.of(probe), Sort.by("id"))) {
            responses.add(toResponse(test));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public QualityTestResponse getTest(Long id) {
        return toResponse(findTest(id));
    }

    // Latest test overall, or for one zone when zoneId is given
    @Transactional(readOnly = true)
    public QualityTestResponse getLatestTest(Long zoneId) {
        QualityTest latest;
        if (zoneId == null) {
            latest = testRepository.findFirstByOrderByTestDateDescIdDesc()
                    .orElseThrow(() -> new ResourceNotFoundException("No quality tests recorded yet"));
        } else {
            latest = testRepository.findFirstByZoneIdOrderByTestDateDescIdDesc(zoneId)
                    .orElseThrow(() -> new ResourceNotFoundException("No quality tests found for zone " + zoneId));
        }
        return toResponse(latest);
    }

    @Transactional
    public QualityTestResponse createTest(QualityTestRequest request) {
        QualityTest test = new QualityTest();
        copyDetails(request, test);
        test.setComplianceStatus(checkCompliance(test));
        return toResponse(testRepository.save(test));
    }

    @Transactional
    public QualityTestResponse updateTest(Long id, QualityTestRequest request) {
        QualityTest test = findTest(id);
        copyDetails(request, test);
        test.setComplianceStatus(checkCompliance(test)); // values may have changed, so check again
        return toResponse(testRepository.save(test));
    }

    @Transactional
    public void deleteTest(Long id) {
        testRepository.delete(findTest(id));
    }

    @Transactional(readOnly = true)
    public ComplianceSummaryResponse getComplianceSummary(Long zoneId) {
        long total;
        long compliant;
        if (zoneId == null) {
            total = testRepository.count();
            compliant = testRepository.countByComplianceStatus(ComplianceStatus.COMPLIANT);
        } else {
            total = testRepository.countByZoneId(zoneId);
            compliant = testRepository.countByZoneIdAndComplianceStatus(zoneId, ComplianceStatus.COMPLIANT);
        }

        ComplianceSummaryResponse summary = new ComplianceSummaryResponse();
        summary.setZoneId(zoneId);
        summary.setTotalTests(total);
        summary.setCompliantTests(compliant);
        summary.setNonCompliantTests(total - compliant);
        if (total > 0) {
            // round to 1 decimal place, e.g. 66.7
            summary.setCompliancePercent(Math.round(compliant * 1000.0 / total) / 10.0);
        }
        return summary;
    }

    // A test is NON_COMPLIANT if bacteria are present or any measured value is outside its standard.
    // Values that were not measured (null) are skipped.
    public ComplianceStatus checkCompliance(QualityTest test) {
        if (Boolean.TRUE.equals(test.getBacteriaPresent())) {
            return ComplianceStatus.NON_COMPLIANT;
        }
        for (QualityStandard standard : standardRepository.findAll()) {
            Double value = getValue(test, standard);
            if (value == null) {
                continue;
            }
            if (value < standard.getMinValue() || value > standard.getMaxValue()) {
                return ComplianceStatus.NON_COMPLIANT;
            }
        }
        return ComplianceStatus.COMPLIANT;
    }

    // ---------- helpers ----------

    private Double getValue(QualityTest test, QualityStandard standard) {
        switch (standard.getParameter()) {
            case PH:
                return test.getPh();
            case TURBIDITY:
                return test.getTurbidity();
            case CHLORINE:
                return test.getChlorine();
            case TDS:
                return test.getTds();
            default:
                return null;
        }
    }

    private QualityTest findTest(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality test not found with id " + id));
    }

    private void copyDetails(QualityTestRequest request, QualityTest test) {
        test.setSourceId(request.getSourceId());
        test.setZoneId(request.getZoneId());
        test.setTestDate(request.getTestDate());
        test.setTestedBy(request.getTestedBy());
        test.setPh(request.getPh());
        test.setTurbidity(request.getTurbidity());
        test.setChlorine(request.getChlorine());
        test.setTds(request.getTds());
        test.setBacteriaPresent(request.getBacteriaPresent());
        test.setRemarks(request.getRemarks());
    }

    private QualityTestResponse toResponse(QualityTest test) {
        QualityTestResponse response = new QualityTestResponse();
        response.setId(test.getId());
        response.setSourceId(test.getSourceId());
        response.setZoneId(test.getZoneId());
        response.setTestDate(test.getTestDate());
        response.setTestedBy(test.getTestedBy());
        response.setPh(test.getPh());
        response.setTurbidity(test.getTurbidity());
        response.setChlorine(test.getChlorine());
        response.setTds(test.getTds());
        response.setBacteriaPresent(test.getBacteriaPresent());
        response.setComplianceStatus(test.getComplianceStatus());
        response.setRemarks(test.getRemarks());
        return response;
    }
}
