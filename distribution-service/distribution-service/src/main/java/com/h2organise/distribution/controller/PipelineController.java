package com.h2organise.distribution.controller;

import com.h2organise.distribution.dto.PipelineRequest;
import com.h2organise.distribution.dto.PipelineResponse;
import com.h2organise.distribution.entity.PipelineStatus;
import com.h2organise.distribution.service.PipelineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pipelines")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineService service;

    // example: GET /pipelines?zoneId=1&status=LEAKING
    @GetMapping
    public List<PipelineResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) PipelineStatus status) {
        return service.findAll(zoneId, status);
    }

    @GetMapping("/{id}")
    public PipelineResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PipelineResponse create(@Valid @RequestBody PipelineRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PipelineResponse update(@PathVariable("id") Long id, @Valid @RequestBody PipelineRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
