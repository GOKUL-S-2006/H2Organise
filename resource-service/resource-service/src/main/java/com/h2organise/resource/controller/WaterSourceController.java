package com.h2organise.resource.controller;

import com.h2organise.resource.dto.CapacityResponse;
import com.h2organise.resource.dto.LowSourceResponse;
import com.h2organise.resource.dto.SourceRequest;
import com.h2organise.resource.dto.SourceResponse;
import com.h2organise.resource.entity.SourceStatus;
import com.h2organise.resource.entity.SourceType;
import com.h2organise.resource.service.WaterSourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for /sources
@RestController
@RequestMapping("/sources")
@RequiredArgsConstructor
public class WaterSourceController {

    private final WaterSourceService service;

    // example: GET /sources?zoneId=1&type=RESERVOIR&status=ACTIVE
    @GetMapping
    public List<SourceResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "type", required = false) SourceType type,
            @RequestParam(name = "status", required = false) SourceStatus status) {
        return service.findAll(zoneId, type, status);
    }

    // sources below their minimum level right now
    // (keep this above /{id} so "low" isn't treated as an id)
    @GetMapping("/low")
    public List<LowSourceResponse> low() {
        return service.findLow();
    }

    @GetMapping("/{id}")
    public SourceResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // total, current and available capacity of one source
    @GetMapping("/{id}/capacity")
    public CapacityResponse capacity(@PathVariable("id") Long id) {
        return service.capacity(id);
    }

    // returns 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SourceResponse create(@Valid @RequestBody SourceRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public SourceResponse update(@PathVariable("id") Long id, @Valid @RequestBody SourceRequest request) {
        return service.update(id, request);
    }

    // returns 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
