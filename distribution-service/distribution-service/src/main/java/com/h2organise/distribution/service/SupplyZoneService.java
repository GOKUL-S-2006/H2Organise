package com.h2organise.distribution.service;

import com.h2organise.distribution.dto.ZoneRequest;
import com.h2organise.distribution.dto.ZoneResponse;
import com.h2organise.distribution.entity.SupplyZone;
import com.h2organise.distribution.exception.ResourceNotFoundException;
import com.h2organise.distribution.repository.SupplyZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// business logic for zones
// readOnly by default, methods that change data have their own @Transactional
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplyZoneService {

    private final SupplyZoneRepository repository;

    public List<ZoneResponse> findAll() {
        return repository.findAll().stream().map(ZoneResponse::from).toList();
    }

    public ZoneResponse findById(Long id) {
        return ZoneResponse.from(getEntity(id));
    }

    // other services in this project (pipeline, schedule...) use this to check the zone exists
    public SupplyZone getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supply zone not found with id " + id));
    }

    @Transactional
    public ZoneResponse create(ZoneRequest request) {
        SupplyZone zone = new SupplyZone();
        apply(zone, request);
        return ZoneResponse.from(repository.save(zone));
    }

    @Transactional
    public ZoneResponse update(Long id, ZoneRequest request) {
        SupplyZone zone = getEntity(id);
        apply(zone, request);
        return ZoneResponse.from(repository.save(zone));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        // flush now so a foreign key error comes out here and not later at commit
        repository.flush();
    }

    // copies the request fields into the entity (used by both create and update)
    private void apply(SupplyZone zone, ZoneRequest r) {
        zone.setName(r.name());
        zone.setDescription(r.description());
        zone.setPopulation(r.population());
        zone.setHouseholds(r.households());
        zone.setPrimarySourceId(r.primarySourceId());
        // if status is not sent, keep what it already has (NORMAL for a new zone)
        if (r.status() != null) {
            zone.setStatus(r.status());
        }
    }
}
