package com.h2organise.maintenance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.maintenance.dto.TreatmentPlantRequest;
import com.h2organise.maintenance.dto.TreatmentPlantResponse;
import com.h2organise.maintenance.entity.PlantStatus;
import com.h2organise.maintenance.entity.TreatmentPlant;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.TreatmentPlantRepository;

@Service
public class TreatmentPlantService {

    private final TreatmentPlantRepository plantRepository;

    public TreatmentPlantService(TreatmentPlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlantResponse> getPlants(Long zoneId) {
        TreatmentPlant probe = new TreatmentPlant();
        probe.setZoneId(zoneId);

        List<TreatmentPlantResponse> responses = new ArrayList<>();
        for (TreatmentPlant plant : plantRepository.findAll(Example.of(probe), Sort.by("id"))) {
            responses.add(toResponse(plant));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public TreatmentPlantResponse getPlant(Long id) {
        return toResponse(findPlant(id));
    }

    @Transactional
    public TreatmentPlantResponse createPlant(TreatmentPlantRequest request) {
        TreatmentPlant plant = new TreatmentPlant();
        copyDetails(request, plant);
        plant.setStatus(request.getStatus() != null ? request.getStatus() : PlantStatus.OPERATIONAL);
        return toResponse(plantRepository.save(plant));
    }

    @Transactional
    public TreatmentPlantResponse updatePlant(Long id, TreatmentPlantRequest request) {
        TreatmentPlant plant = findPlant(id);
        copyDetails(request, plant);
        if (request.getStatus() != null) {
            plant.setStatus(request.getStatus());
        }
        return toResponse(plantRepository.save(plant));
    }

    @Transactional
    public void deletePlant(Long id) {
        plantRepository.delete(findPlant(id));
    }

    private TreatmentPlant findPlant(Long id) {
        return plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plant not found with id " + id));
    }

    private void copyDetails(TreatmentPlantRequest request, TreatmentPlant plant) {
        plant.setName(request.getName().trim());
        plant.setZoneId(request.getZoneId());
        plant.setCapacityMld(request.getCapacityMld());
        plant.setLastServicedDate(request.getLastServicedDate());
    }

    private TreatmentPlantResponse toResponse(TreatmentPlant plant) {
        TreatmentPlantResponse response = new TreatmentPlantResponse();
        response.setId(plant.getId());
        response.setName(plant.getName());
        response.setZoneId(plant.getZoneId());
        response.setCapacityMld(plant.getCapacityMld());
        response.setLastServicedDate(plant.getLastServicedDate());
        response.setStatus(plant.getStatus());
        return response;
    }
}
