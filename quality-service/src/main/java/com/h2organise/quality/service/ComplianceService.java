package com.h2organise.quality.service;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import com.h2organise.quality.repository.QualityTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final QualityTestRepository repository;

    /** Share of COMPLIANT tests, overall or for one zone, rounded to one decimal place. */
    @Transactional(readOnly = true)
    public ComplianceSummaryResponse summary(Long zoneId) {
        long total;
        long compliant;
        if (zoneId == null) {
            total = repository.count();
            compliant = repository.countByComplianceStatus(ComplianceStatus.COMPLIANT);
        } else {
            total = repository.countByZoneId(zoneId);
            compliant = repository.countByZoneIdAndComplianceStatus(zoneId, ComplianceStatus.COMPLIANT);
        }
        double percent = total == 0 ? 0.0 : Math.round(compliant * 1000.0 / total) / 10.0;
        return new ComplianceSummaryResponse(zoneId, total, compliant, total - compliant, percent);
    }
}
