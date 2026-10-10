package com.h2organise.distribution.controller;

import com.h2organise.distribution.dto.ScheduleRequest;
import com.h2organise.distribution.dto.ScheduleResponse;
import com.h2organise.distribution.entity.ScheduleStatus;
import com.h2organise.distribution.service.DistributionScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final DistributionScheduleService service;

    // example: GET /schedules?zoneId=1&status=PLANNED&date=2026-10-10
    @GetMapping
    public List<ScheduleResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) ScheduleStatus status,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findAll(zoneId, status, date);
    }

    @GetMapping("/{id}")
    public ScheduleResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse create(@Valid @RequestBody ScheduleRequest request) {
        return service.create(request);
    }

    // full update, so the caller must send all the fields (not just the changed one)
    @PutMapping("/{id}")
    public ScheduleResponse update(@PathVariable("id") Long id, @Valid @RequestBody ScheduleRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
