package com.h2organise.quality.service;

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.Inspection;
import com.h2organise.quality.entity.InspectionResult;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.InspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InspectionService {

    private final InspectionRepository repository;

    // zoneId and result are both optional filters
    public List<InspectionResponse> findAll(Long zoneId, InspectionResult result) {
        Specification<Inspection> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (result != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("result"), result));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(InspectionResponse::from).toList();
    }

    // inspections whose next due date is between today and 30 days from now
    public List<InspectionResponse> findDue() {
        LocalDate today = LocalDate.now();
        return repository.findByNextDueDateBetweenOrderByNextDueDate(today, today.plusDays(30))
                .stream().map(InspectionResponse::from).toList();
    }

    public InspectionResponse findById(Long id) {
        return InspectionResponse.from(getEntity(id));
    }

    private Inspection getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id " + id));
    }

    @Transactional
    public InspectionResponse create(InspectionRequest request) {
        Inspection inspection = new Inspection();
        apply(inspection, request);
        return InspectionResponse.from(repository.save(inspection));
    }

    @Transactional
    public InspectionResponse update(Long id, InspectionRequest request) {
        Inspection inspection = getEntity(id);
        apply(inspection, request);
        return InspectionResponse.from(repository.save(inspection));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private void apply(Inspection i, InspectionRequest r) {
        i.setSourceId(r.sourceId());
        i.setZoneId(r.zoneId());
        i.setInspectionDate(r.inspectionDate());
        i.setInspectorName(r.inspectorName());
        i.setResult(r.result());
        i.setRemarks(r.remarks());
        i.setNextDueDate(r.nextDueDate());
    }
}
