package com.h2organise.distribution.service;

import com.h2organise.distribution.dto.PipelineRequest;
import com.h2organise.distribution.dto.PipelineResponse;
import com.h2organise.distribution.entity.Pipeline;
import com.h2organise.distribution.entity.PipelineStatus;
import com.h2organise.distribution.exception.ResourceNotFoundException;
import com.h2organise.distribution.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PipelineService {

    private final PipelineRepository repository;
    private final SupplyZoneService zoneService;

    // zoneId and status are both optional filters
    public List<PipelineResponse> findAll(Long zoneId, PipelineStatus status) {
        // start with "match everything" and add conditions only if the filter was given
        Specification<Pipeline> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zone").get("id"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        return repository.findAll(spec, Sort.by("id")).stream().map(PipelineResponse::from).toList();
    }

    public PipelineResponse findById(Long id) {
        return PipelineResponse.from(getEntity(id));
    }

    private Pipeline getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found with id " + id));
    }

    @Transactional
    public PipelineResponse create(PipelineRequest request) {
        Pipeline pipeline = new Pipeline();
        apply(pipeline, request);
        return PipelineResponse.from(repository.save(pipeline));
    }

    @Transactional
    public PipelineResponse update(Long id, PipelineRequest request) {
        Pipeline pipeline = getEntity(id);
        apply(pipeline, request);
        return PipelineResponse.from(repository.save(pipeline));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        repository.flush();
    }

    private void apply(Pipeline p, PipelineRequest r) {
        p.setName(r.name());
        // this throws 404 if the zone id doesn't exist
        p.setZone(zoneService.getEntity(r.zoneId()));
        p.setSourceId(r.sourceId());
        p.setLengthKm(r.lengthKm());
        p.setDiameterMm(r.diameterMm());
        p.setMaterial(r.material());
        p.setInstalledDate(r.installedDate());
        if (r.status() != null) {
            p.setStatus(r.status());
        }
    }
}
