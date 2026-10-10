package com.h2organise.quality.service;

import com.h2organise.quality.dto.StandardRequest;
import com.h2organise.quality.dto.StandardResponse;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.exception.BadRequestException;
import com.h2organise.quality.repository.QualityStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StandardService {

    private final QualityStandardRepository repository;

    @Transactional(readOnly = true)
    public List<StandardResponse> getAll() {
        return repository.findAll(Sort.by("id")).stream().map(StandardResponse::from).toList();
    }

    /**
     * Adjust the range of one parameter. If that parameter has no row yet it is created.
     * Tests that were already saved keep their old compliance result.
     */
    @Transactional
    public StandardResponse update(StandardRequest request) {
        if (request.minValue() == null && request.maxValue() == null) {
            throw new BadRequestException("At least one of minValue or maxValue is required");
        }
        if (request.minValue() != null && request.maxValue() != null
                && request.minValue() > request.maxValue()) {
            throw new BadRequestException("minValue cannot be greater than maxValue");
        }
        QualityStandard standard = repository.findByParameter(request.parameter())
                .orElseGet(() -> QualityStandard.builder().parameter(request.parameter()).build());
        standard.setMinValue(request.minValue());
        standard.setMaxValue(request.maxValue());
        return StandardResponse.from(repository.save(standard));
    }
}
