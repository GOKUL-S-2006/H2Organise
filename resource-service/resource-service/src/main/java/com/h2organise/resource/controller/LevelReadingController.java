package com.h2organise.resource.controller;

import com.h2organise.resource.dto.LevelRequest;
import com.h2organise.resource.dto.LevelResponse;
import com.h2organise.resource.service.LevelReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// REST endpoints for /levels (water level readings)
@RestController
@RequestMapping("/levels")
@RequiredArgsConstructor
public class LevelReadingController {

    private final LevelReadingService service;

    // example: GET /levels?sourceId=1&from=2026-10-07&to=2026-10-10
    @GetMapping
    public List<LevelResponse> list(
            @RequestParam(name = "sourceId", required = false) Long sourceId,
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.findAll(sourceId, from, to);
    }

    // newest reading per source, this is what the zone status page calls
    // example: GET /levels/latest?zoneId=1   or   GET /levels/latest?sourceId=1
    @GetMapping("/latest")
    public List<LevelResponse> latest(
            @RequestParam(name = "sourceId", required = false) Long sourceId,
            @RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.latest(sourceId, zoneId);
    }

    // returns 201 Created, levelPercent is calculated by the service
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelResponse create(@Valid @RequestBody LevelRequest request) {
        return service.create(request);
    }
}
