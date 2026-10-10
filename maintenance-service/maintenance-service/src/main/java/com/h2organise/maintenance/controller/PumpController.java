package com.h2organise.maintenance.controller;

import com.h2organise.maintenance.dto.PumpRequest;
import com.h2organise.maintenance.dto.PumpResponse;
import com.h2organise.maintenance.entity.PumpStatus;
import com.h2organise.maintenance.service.PumpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pumps")
@RequiredArgsConstructor
public class PumpController {

    private final PumpService service;

    // example: GET /pumps?zoneId=1&status=FAULTY
    @GetMapping
    public List<PumpResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) PumpStatus status) {
        return service.findAll(zoneId, status);
    }

    @GetMapping("/{id}")
    public PumpResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PumpResponse create(@Valid @RequestBody PumpRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PumpResponse update(@PathVariable("id") Long id, @Valid @RequestBody PumpRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
