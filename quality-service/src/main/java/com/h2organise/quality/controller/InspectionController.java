package com.h2organise.quality.controller;

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.Inspection.Result;
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

    @GetMapping
    public List<InspectionResponse> list(@RequestParam(required = false) Long zoneId,
                                         @RequestParam(required = false) Result result) {
        return service.list(zoneId, result);
    }

    @GetMapping("/due")
    public List<InspectionResponse> due(@RequestParam(required = false) Long zoneId) {
        return service.due(zoneId);
    }

    @GetMapping("/{id}")
    public InspectionResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InspectionResponse create(@Valid @RequestBody InspectionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public InspectionResponse update(@PathVariable Long id, @Valid @RequestBody InspectionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
