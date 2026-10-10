package com.h2organise.quality.controller;

import com.h2organise.quality.dto.QualityTestRequest;
import com.h2organise.quality.dto.QualityTestResponse;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import com.h2organise.quality.service.QualityTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class QualityTestController {

    private final QualityTestService service;

    @GetMapping
    public List<QualityTestResponse> list(@RequestParam(required = false) Long zoneId,
                                          @RequestParam(required = false) Long sourceId,
                                          @RequestParam(required = false) ComplianceStatus status) {
        return service.list(zoneId, sourceId, status);
    }

    /** "latest" is a fixed word, so Spring prefers it over the /{id} pattern. */
    @GetMapping("/latest")
    public QualityTestResponse latest(@RequestParam(required = false) Long zoneId) {
        return service.latest(zoneId);
    }

    @GetMapping("/{id}")
    public QualityTestResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QualityTestResponse create(@Valid @RequestBody QualityTestRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public QualityTestResponse update(@PathVariable Long id, @Valid @RequestBody QualityTestRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
