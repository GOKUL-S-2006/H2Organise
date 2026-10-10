package com.h2organise.distribution.controller;

import com.h2organise.distribution.dto.CategorySummary;
import com.h2organise.distribution.dto.ConsumptionRequest;
import com.h2organise.distribution.dto.ConsumptionResponse;
import com.h2organise.distribution.entity.ConsumptionCategory;
import com.h2organise.distribution.service.ConsumptionRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consumption")
@RequiredArgsConstructor
public class ConsumptionController {

    private final ConsumptionRecordService service;

    // example: GET /consumption?zoneId=1&category=DOMESTIC&from=2026-10-01&to=2026-10-10
    @GetMapping
    public List<ConsumptionResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "category", required = false) ConsumptionCategory category,
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.findAll(zoneId, category, from, to);
    }

    // totals by category, used for the dashboard chart
    // (keep this above /{id} so "summary" isn't treated as an id)
    @GetMapping("/summary")
    public List<CategorySummary> summary(@RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.summary(zoneId);
    }

    @GetMapping("/{id}")
    public ConsumptionResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsumptionResponse create(@Valid @RequestBody ConsumptionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ConsumptionResponse update(@PathVariable("id") Long id, @Valid @RequestBody ConsumptionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
