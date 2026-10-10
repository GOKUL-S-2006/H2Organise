package com.h2organise.quality.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.h2organise.quality.dto.QualityStandardRequest;
import com.h2organise.quality.dto.QualityStandardResponse;
import com.h2organise.quality.service.QualityStandardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/standards")
public class QualityStandardController {

    private final QualityStandardService standardService;

    public QualityStandardController(QualityStandardService standardService) {
        this.standardService = standardService;
    }

    @GetMapping
    public List<QualityStandardResponse> getStandards() {
        return standardService.getStandards();
    }

    // body: { "parameter": "PH", "minValue": 6.5, "maxValue": 8.5 }
    @PutMapping
    public QualityStandardResponse updateStandard(@Valid @RequestBody QualityStandardRequest request) {
        return standardService.updateStandard(request);
    }
}
