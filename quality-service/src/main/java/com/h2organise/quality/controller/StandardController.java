package com.h2organise.quality.controller;

import com.h2organise.quality.dto.StandardRequest;
import com.h2organise.quality.dto.StandardResponse;
import com.h2organise.quality.service.StandardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standards")
@RequiredArgsConstructor
public class StandardController {

    private final StandardService service;

    @GetMapping
    public List<StandardResponse> getAll() {
        return service.getAll();
    }

    @PutMapping
    public StandardResponse update(@Valid @RequestBody StandardRequest request) {
        return service.update(request);
    }
}
