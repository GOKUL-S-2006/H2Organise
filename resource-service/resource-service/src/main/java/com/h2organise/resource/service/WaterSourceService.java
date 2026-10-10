package com.h2organise.resource.service;

import com.h2organise.resource.dto.CapacityResponse;
import com.h2organise.resource.dto.LowSourceResponse;
import com.h2organise.resource.dto.SourceRequest;
import com.h2organise.resource.dto.SourceResponse;
import com.h2organise.resource.entity.LevelReading;
import com.h2organise.resource.entity.SourceStatus;
import com.h2organise.resource.entity.SourceType;
import com.h2organise.resource.entity.WaterSource;
import com.h2organise.resource.exception.ResourceNotFoundException;
import com.h2organise.resource.repository.LevelReadingRepository;
import com.h2organise.resource.repository.WaterSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// business logic for water sources
// readOnly by default, methods that change data have their own @Transactional
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterSourceService {

    private final WaterSourceRepository repository;
    private final LevelReadingRepository levelRepository;

    // zoneId, type and status are all optional filters
    public List<SourceResponse> findAll(Long zoneId, SourceType type, SourceStatus status) {
        return findEntities(zoneId, type, status).stream().map(SourceResponse::from).toList();
    }

    // same as above but returns entities, the level service uses this to find the latest readings
    public List<WaterSource> findEntities(Long zoneId, SourceType type, SourceStatus status) {
        // start with "match everything" and add conditions only if the filter was given
        Specification<WaterSource> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        return repository.findAll(spec, Sort.by("id"));
    }

    public SourceResponse findById(Long id) {
        return SourceResponse.from(getEntity(id));
    }

    public WaterSource getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Water source not found with id " + id));
    }

    // sources whose newest reading is below their own minimum limit
    // the web-app uses this to colour zones red on the dashboard
    public List<LowSourceResponse> findLow() {
        List<LowSourceResponse> result = new ArrayList<>();
        for (WaterSource s : repository.findAll(Sort.by("id"))) {
            Optional<LevelReading> latest = levelRepository.findFirstBySource_IdOrderByReadingTimeDesc(s.getId());
            if (latest.isPresent() && latest.get().getLevelPercent() < s.getMinThresholdPercent()) {
                LevelReading r = latest.get();
                result.add(new LowSourceResponse(s.getId(), s.getName(), s.getZoneId(),
                        r.getLevelPercent(), s.getMinThresholdPercent(), r.getReadingTime()));
            }
        }
        return result;
    }

    // total, used and free water of one source
    public CapacityResponse capacity(Long id) {
        WaterSource s = getEntity(id);
        Optional<LevelReading> latest = levelRepository.findFirstBySource_IdOrderByReadingTimeDesc(id);
        if (latest.isEmpty()) {
            // no reading yet, so we only know the total
            return new CapacityResponse(s.getId(), s.getName(), s.getTotalCapacity(),
                    null, null, null, s.getMinThresholdPercent(), null);
        }
        LevelReading r = latest.get();
        return new CapacityResponse(s.getId(), s.getName(), s.getTotalCapacity(), r.getCurrentVolume(),
                s.getTotalCapacity() - r.getCurrentVolume(), r.getLevelPercent(),
                s.getMinThresholdPercent(), r.getReadingTime());
    }

    @Transactional
    public SourceResponse create(SourceRequest request) {
        WaterSource source = new WaterSource();
        apply(source, request);
        return SourceResponse.from(repository.save(source));
    }

    @Transactional
    public SourceResponse update(Long id, SourceRequest request) {
        WaterSource source = getEntity(id);
        apply(source, request);
        return SourceResponse.from(repository.save(source));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        // flush now so a foreign key error comes out here and not later at commit
        repository.flush();
    }

    // copies the request fields into the entity (used by both create and update)
    private void apply(WaterSource s, SourceRequest r) {
        s.setName(r.name());
        s.setType(r.type());
        s.setLocation(r.location());
        s.setLatitude(r.latitude());
        s.setLongitude(r.longitude());
        s.setTotalCapacity(r.totalCapacity());
        s.setZoneId(r.zoneId());
        s.setCommissionedDate(r.commissionedDate());
        // if these are not sent, keep what the entity already has (30% and ACTIVE for a new source)
        if (r.minThresholdPercent() != null) {
            s.setMinThresholdPercent(r.minThresholdPercent());
        }
        if (r.status() != null) {
            s.setStatus(r.status());
        }
    }
}
