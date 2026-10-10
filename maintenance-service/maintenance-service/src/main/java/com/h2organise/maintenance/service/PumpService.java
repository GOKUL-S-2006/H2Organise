package com.h2organise.maintenance.service;

import com.h2organise.maintenance.dto.PumpRequest;
import com.h2organise.maintenance.dto.PumpResponse;
import com.h2organise.maintenance.entity.Pump;
import com.h2organise.maintenance.entity.PumpStatus;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.PumpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PumpService {

    private final PumpRepository repository;

    // zoneId and status are both optional filters
    public List<PumpResponse> findAll(Long zoneId, PumpStatus status) {
        Specification<Pump> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(PumpResponse::from).toList();
    }

    public PumpResponse findById(Long id) {
        return PumpResponse.from(getEntity(id));
    }

    private Pump getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pump not found with id " + id));
    }

    @Transactional
    public PumpResponse create(PumpRequest request) {
        Pump pump = new Pump();
        apply(pump, request);
        return PumpResponse.from(repository.save(pump));
    }

    @Transactional
    public PumpResponse update(Long id, PumpRequest request) {
        Pump pump = getEntity(id);
        apply(pump, request);
        return PumpResponse.from(repository.save(pump));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private void apply(Pump p, PumpRequest r) {
        p.setName(r.name());
        p.setZoneId(r.zoneId());
        p.setSourceId(r.sourceId());
        p.setCapacityLph(r.capacityLph());
        p.setInstalledDate(r.installedDate());
        if (r.status() != null) {
            p.setStatus(r.status());
        }
    }
}
