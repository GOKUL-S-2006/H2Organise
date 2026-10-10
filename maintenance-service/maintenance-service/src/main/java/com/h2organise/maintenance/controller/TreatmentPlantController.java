package com.h2organise.maintenance.controller;

import com.h2organise.maintenance.dto.TreatmentPlantRequest;
import com.h2organise.maintenance.dto.TreatmentPlantResponse;
import com.h2organise.maintenance.service.TreatmentPlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
public class TreatmentPlantController {

    private final TreatmentPlantService service;

    // example: GET /plants?zoneId=1
    @GetMapping
    public List<TreatmentPlantResponse> list(@RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.findAll(zoneId);
    }

    @GetMapping("/{id}")
    public TreatmentPlantResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreatmentPlantResponse create(@Valid @RequestBody TreatmentPlantRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TreatmentPlantResponse update(@PathVariable("id") Long id, @Valid @RequestBody TreatmentPlantRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
