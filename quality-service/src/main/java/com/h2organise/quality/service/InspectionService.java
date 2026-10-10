package com.h2organise.quality.service;

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.Inspection;
import com.h2organise.quality.entity.Inspection.Result;
import com.h2organise.quality.exception.BadRequestException;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.InspectionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionService {

    /** "Due" means the next due date is within this many days from today. */
    static final int DUE_WITHIN_DAYS = 30;

    private final InspectionRepository repository;

    @Transactional(readOnly = true)
    public List<InspectionResponse> list(Long zoneId, Result result) {
        Specification<Inspection> filter = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (zoneId != null) {
                predicates.add(cb.equal(root.get("zoneId"), zoneId));
            }
            if (result != null) {
                predicates.add(cb.equal(root.get("result"), result));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Direction.DESC, "inspectionDate").and(Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(filter, sort).stream().map(InspectionResponse::from).toList();
    }

    /**
     * Inspections whose next due date is today or earlier (overdue) or within the next 30 days.
     * Soonest first. Optional zone filter.
     */
    @Transactional(readOnly = true)
    public List<InspectionResponse> due(Long zoneId) {
        LocalDate limit = LocalDate.now().plusDays(DUE_WITHIN_DAYS);
        Specification<Inspection> filter = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNotNull(root.get("nextDueDate")));
            predicates.add(cb.lessThanOrEqualTo(root.<LocalDate>get("nextDueDate"), limit));
            if (zoneId != null) {
                predicates.add(cb.equal(root.get("zoneId"), zoneId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Direction.ASC, "nextDueDate").and(Sort.by(Sort.Direction.ASC, "id"));
        return repository.findAll(filter, sort).stream().map(InspectionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InspectionResponse getById(Long id) {
        return InspectionResponse.from(find(id));
    }

    @Transactional
    public InspectionResponse create(InspectionRequest request) {
        Inspection inspection = new Inspection();
        apply(inspection, request);
        return InspectionResponse.from(repository.save(inspection));
    }

    @Transactional
    public InspectionResponse update(Long id, InspectionRequest request) {
        Inspection inspection = find(id);
        apply(inspection, request);
        return InspectionResponse.from(repository.save(inspection));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Inspection find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection " + id + " not found"));
    }

    private void apply(Inspection inspection, InspectionRequest r) {
        if (r.nextDueDate() != null && r.nextDueDate().isBefore(r.inspectionDate())) {
            throw new BadRequestException("nextDueDate cannot be before inspectionDate");
        }
        inspection.setSourceId(r.sourceId());
        inspection.setZoneId(r.zoneId());
        inspection.setInspectionDate(r.inspectionDate());
        inspection.setInspectorName(r.inspectorName());
        inspection.setResult(r.result());
        inspection.setRemarks(r.remarks());
        inspection.setNextDueDate(r.nextDueDate());
    }
}
