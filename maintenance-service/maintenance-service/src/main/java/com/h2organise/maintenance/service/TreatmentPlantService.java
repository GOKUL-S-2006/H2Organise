package com.h2organise.maintenance.service;

import com.h2organise.maintenance.dto.TreatmentPlantRequest;
import com.h2organise.maintenance.dto.TreatmentPlantResponse;
import com.h2organise.maintenance.entity.TreatmentPlant;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.TreatmentPlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TreatmentPlantService {

    private final TreatmentPlantRepository repository;

    public List<TreatmentPlantResponse> findAll(Long zoneId) {
        List<TreatmentPlant> plants = zoneId == null
                ? repository.findAll(Sort.by("id"))
                : repository.findByZoneId(zoneId, Sort.by("id"));
        return plants.stream().map(TreatmentPlantResponse::from).toList();
    }

    public TreatmentPlantResponse findById(Long id) {
        return TreatmentPlantResponse.from(getEntity(id));
    }

    private TreatmentPlant getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plant not found with id " + id));
    }

    @Transactional
    public TreatmentPlantResponse create(TreatmentPlantRequest request) {
        TreatmentPlant plant = new TreatmentPlant();
        apply(plant, request);
        return TreatmentPlantResponse.from(repository.save(plant));
    }

    @Transactional
    public TreatmentPlantResponse update(Long id, TreatmentPlantRequest request) {
        TreatmentPlant plant = getEntity(id);
        apply(plant, request);
        return TreatmentPlantResponse.from(repository.save(plant));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private void apply(TreatmentPlant p, TreatmentPlantRequest r) {
        p.setName(r.name());
        p.setZoneId(r.zoneId());
        p.setCapacityMld(r.capacityMld());
        p.setLastServicedDate(r.lastServicedDate());
        if (r.status() != null) {
            p.setStatus(r.status());
        }
    }
}
