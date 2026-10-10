package com.h2organise.maintenance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h2organise.maintenance.dto.TreatmentPlantRequest;
import com.h2organise.maintenance.dto.TreatmentPlantResponse;
import com.h2organise.maintenance.service.TreatmentPlantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/plants")
public class TreatmentPlantController {

    private final TreatmentPlantService plantService;

    public TreatmentPlantController(TreatmentPlantService plantService) {
        this.plantService = plantService;
    }

    // GET /plants?zoneId=1 (optional)
    @GetMapping
    public List<TreatmentPlantResponse> getPlants(@RequestParam(required = false) Long zoneId) {
        return plantService.getPlants(zoneId);
    }

    @GetMapping("/{id}")
    public TreatmentPlantResponse getPlant(@PathVariable Long id) {
        return plantService.getPlant(id);
    }

    @PostMapping
    public ResponseEntity<TreatmentPlantResponse> createPlant(@Valid @RequestBody TreatmentPlantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantService.createPlant(request));
    }

    @PutMapping("/{id}")
    public TreatmentPlantResponse updatePlant(@PathVariable Long id, @Valid @RequestBody TreatmentPlantRequest request) {
        return plantService.updatePlant(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }
}
