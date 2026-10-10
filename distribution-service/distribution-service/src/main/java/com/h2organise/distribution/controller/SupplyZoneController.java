package com.h2organise.distribution.controller;

import com.h2organise.distribution.dto.ZoneRequest;
import com.h2organise.distribution.dto.ZoneResponse;
import com.h2organise.distribution.service.SupplyZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for /zones
@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class SupplyZoneController {

    private final SupplyZoneService service;

    @GetMapping
    public List<ZoneResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ZoneResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // returns 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZoneResponse create(@Valid @RequestBody ZoneRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ZoneResponse update(@PathVariable("id") Long id, @Valid @RequestBody ZoneRequest request) {
        return service.update(id, request);
    }

    // returns 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
