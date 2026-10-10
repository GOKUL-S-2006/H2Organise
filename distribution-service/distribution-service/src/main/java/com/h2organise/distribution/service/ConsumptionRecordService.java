package com.h2organise.distribution.service;

import com.h2organise.distribution.dto.CategorySummary;
import com.h2organise.distribution.dto.ConsumptionRequest;
import com.h2organise.distribution.dto.ConsumptionResponse;
import com.h2organise.distribution.entity.ConsumptionCategory;
import com.h2organise.distribution.entity.ConsumptionRecord;
import com.h2organise.distribution.exception.ResourceNotFoundException;
import com.h2organise.distribution.repository.ConsumptionRecordRepository;
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
public class ConsumptionRecordService {

    private final ConsumptionRecordRepository repository;
    private final SupplyZoneService zoneService;

    // filters: zone, category, and a date range (from / to are both included)
    public List<ConsumptionResponse> findAll(Long zoneId, ConsumptionCategory category,
                                             LocalDate from, LocalDate to) {
        Specification<ConsumptionRecord> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zone").get("id"), zoneId));
        }
        if (category != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        if (from != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("recordDate"), from));
        }
        if (to != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("recordDate"), to));
        }
        // newest first
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "recordDate")).stream()
                .map(ConsumptionResponse::from).toList();
    }

    // used for the dashboard chart (usage by category)
    public List<CategorySummary> summary(Long zoneId) {
        return zoneId == null ? repository.summarizeAll() : repository.summarizeByZone(zoneId);
    }

    public ConsumptionResponse findById(Long id) {
        return ConsumptionResponse.from(getEntity(id));
    }

    private ConsumptionRecord getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumption record not found with id " + id));
    }

    @Transactional
    public ConsumptionResponse create(ConsumptionRequest request) {
        ConsumptionRecord record = new ConsumptionRecord();
        apply(record, request);
        return ConsumptionResponse.from(repository.save(record));
    }

    @Transactional
    public ConsumptionResponse update(Long id, ConsumptionRequest request) {
        ConsumptionRecord record = getEntity(id);
        apply(record, request);
        return ConsumptionResponse.from(repository.save(record));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        repository.flush();
    }

    private void apply(ConsumptionRecord c, ConsumptionRequest r) {
        c.setZone(zoneService.getEntity(r.zoneId()));
        c.setRecordDate(r.recordDate());
        c.setCategory(r.category());
        c.setVolumeConsumed(r.volumeConsumed());
    }
}
