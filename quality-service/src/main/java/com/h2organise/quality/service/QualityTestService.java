package com.h2organise.quality.service;

import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import com.h2organise.quality.exception.BadRequestException;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.QualityTestRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QualityTestService {

    private static final Sort NEWEST_FIRST =
            Sort.by(Sort.Direction.DESC, "testDate").and(Sort.by(Sort.Direction.DESC, "id"));

    private final QualityTestRepository repository;
    private final ComplianceEvaluator evaluator;

    @Transactional(readOnly = true)
    public List<QualityTestResponse> list(Long zoneId, Long sourceId, ComplianceStatus status) {
        Specification<QualityTest> filter = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (zoneId != null) {
                predicates.add(cb.equal(root.get("zoneId"), zoneId));
            }
            if (sourceId != null) {
                predicates.add(cb.equal(root.get("sourceId"), sourceId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("complianceStatus"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return repository.findAll(filter, NEWEST_FIRST).stream().map(QualityTestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public QualityTestResponse getById(Long id) {
        return QualityTestResponse.from(find(id));
    }

    /** Latest test overall, or for one zone when zoneId is given. 404 when there is none yet. */
    @Transactional(readOnly = true)
    public QualityTestResponse latest(Long zoneId) {
        return (zoneId == null
                ? repository.findFirstByOrderByTestDateDescIdDesc()
                : repository.findFirstByZoneIdOrderByTestDateDescIdDesc(zoneId))
                .map(QualityTestResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        zoneId == null ? "No quality tests found" : "No quality tests found for zone " + zoneId));
    }

    @Transactional
    public QualityTestResponse create(QualityTestRequest request) {
        QualityTest test = new QualityTest();
        applyAndEvaluate(test, request);
        return QualityTestResponse.from(repository.save(test));
    }

    @Transactional
    public QualityTestResponse update(Long id, QualityTestRequest request) {
        QualityTest test = find(id);
        applyAndEvaluate(test, request);
        return QualityTestResponse.from(repository.save(test));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private QualityTest find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality test " + id + " not found"));
    }

    private void applyAndEvaluate(QualityTest test, QualityTestRequest r) {
        if (r.ph() == null && r.turbidity() == null && r.chlorine() == null
                && r.tds() == null && r.bacteriaPresent() == null) {
            throw new BadRequestException(
                    "Provide at least one measurement: ph, turbidity, chlorine, tds or bacteriaPresent");
        }
        test.setSourceId(r.sourceId());
        test.setZoneId(r.zoneId());
        test.setTestDate(r.testDate());
        test.setTestedBy(r.testedBy());
        test.setPh(r.ph());
        test.setTurbidity(r.turbidity());
        test.setChlorine(r.chlorine());
        test.setTds(r.tds());
        test.setBacteriaPresent(Boolean.TRUE.equals(r.bacteriaPresent()));
        test.setRemarks(r.remarks());

        // The client never sends this field. The service always decides it.
        List<String> problems = evaluator.violations(test);
        test.setComplianceStatus(problems.isEmpty() ? ComplianceStatus.COMPLIANT : ComplianceStatus.NON_COMPLIANT);
        if (!problems.isEmpty()) {
            log.info("Test for zone {} is NON_COMPLIANT: {}", r.zoneId(), String.join("; ", problems));
        }
    }
}
