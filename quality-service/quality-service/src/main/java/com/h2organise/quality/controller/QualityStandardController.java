package com.h2organise.quality.controller;

import com.h2organise.quality.dto.QualityStandardRequest;
import com.h2organise.quality.dto.QualityStandardResponse;
import com.h2organise.quality.service.QualityStandardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standards")
@RequiredArgsConstructor
public class QualityStandardController {

    private final QualityStandardService service;

    @GetMapping
    public List<QualityStandardResponse> list() {
        return service.findAll();
    }

    @PutMapping
    public QualityStandardResponse update(@Valid @RequestBody QualityStandardRequest request) {
        return service.update(request);
    }
}
