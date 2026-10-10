package com.h2organise.quality.service;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.ComplianceStatus;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.QualityStandardRepository;
import com.h2organise.quality.repository.QualityTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QualityTestService {

    private final QualityTestRepository repository;
    private final QualityStandardRepository standardRepository;

    // zoneId, sourceId and status are all optional filters
    public List<QualityTestResponse> findAll(Long zoneId, Long sourceId, ComplianceStatus status) {
        Specification<QualityTest> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (sourceId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sourceId"), sourceId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("complianceStatus"), status));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(QualityTestResponse::from).toList();
    }

    public QualityTestResponse findById(Long id) {
        return QualityTestResponse.from(getEntity(id));
    }

    // latest test for a zone, or across all zones if zoneId is not given
    public QualityTestResponse findLatest(Long zoneId) {
        QualityTest latest = (zoneId == null
                ? repository.findFirstByOrderByTestDateDescIdDesc()
                : repository.findFirstByZoneIdOrderByTestDateDescIdDesc(zoneId))
                .orElseThrow(() -> new ResourceNotFoundException("No quality tests found"));
        return QualityTestResponse.from(latest);
    }

    private QualityTest getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality test not found with id " + id));
    }

    @Transactional
    public QualityTestResponse create(QualityTestRequest request) {
        QualityTest test = new QualityTest();
        apply(test, request);
        return QualityTestResponse.from(repository.save(test));
    }

    @Transactional
    public QualityTestResponse update(Long id, QualityTestRequest request) {
        QualityTest test = getEntity(id);
        apply(test, request);
        return QualityTestResponse.from(repository.save(test));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    public ComplianceSummaryResponse summary(Long zoneId) {
        List<QualityTest> tests = zoneId == null ? repository.findAll() : repository.findByZoneId(zoneId);
        long total = tests.size();
        long compliant = tests.stream().filter(t -> t.getComplianceStatus() == ComplianceStatus.COMPLIANT).count();
        // one decimal place, e.g. 66.7
        double percent = total == 0 ? 0 : Math.round(compliant * 1000.0 / total) / 10.0;
        return new ComplianceSummaryResponse(zoneId, total, compliant, total - compliant, percent);
    }

    private void apply(QualityTest t, QualityTestRequest r) {
        t.setSourceId(r.sourceId());
        t.setZoneId(r.zoneId());
        t.setTestDate(r.testDate());
        t.setTestedBy(r.testedBy());
        t.setPh(r.ph());
        t.setTurbidity(r.turbidity());
        t.setChlorine(r.chlorine());
        t.setTds(r.tds());
        t.setBacteriaPresent(r.bacteriaPresent());
        t.setRemarks(r.remarks());
        t.setComplianceStatus(checkCompliance(t));
    }

    // non compliant if bacteria are found or any value is outside its standard range
    // values that were not measured (null) are skipped
    private ComplianceStatus checkCompliance(QualityTest t) {
        if (Boolean.TRUE.equals(t.getBacteriaPresent())) {
            return ComplianceStatus.NON_COMPLIANT;
        }
        for (QualityStandard s : standardRepository.findAll()) {
            Double value = switch (s.getParameter()) {
                case PH -> t.getPh();
                case TURBIDITY -> t.getTurbidity();
                case CHLORINE -> t.getChlorine();
                case TDS -> t.getTds();
            };
            if (value != null && (value < s.getMinValue() || value > s.getMaxValue())) {
                return ComplianceStatus.NON_COMPLIANT;
            }
        }
        return ComplianceStatus.COMPLIANT;
    }
}
