package com.h2organise.quality.controller;

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.InspectionResult;
import com.h2organise.quality.service.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspections")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService service;

    // example: GET /inspections?zoneId=1&result=PASSED
    @GetMapping
    public List<InspectionResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "result", required = false) InspectionResult result) {
        return service.findAll(zoneId, result);
    }

    // due in the next 30 days
    @GetMapping("/due")
    public List<InspectionResponse> due() {
        return service.findDue();
    }

    @GetMapping("/{id}")
    public InspectionResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InspectionResponse create(@Valid @RequestBody InspectionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public InspectionResponse update(@PathVariable("id") Long id, @Valid @RequestBody InspectionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
