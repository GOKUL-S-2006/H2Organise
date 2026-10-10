package com.h2organise.quality.controller;

import com.h2organise.quality.dto.ComplianceSummaryResponse;
import com.h2organise.quality.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService service;

    @GetMapping("/summary")
    public ComplianceSummaryResponse summary(@RequestParam(required = false) Long zoneId) {
        return service.summary(zoneId);
    }
}
