package com.h2organise.quality.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.quality.dto.QualityStandardRequest;
import com.h2organise.quality.dto.QualityStandardResponse;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.QualityStandardRepository;

@Service
public class QualityStandardService {

    private final QualityStandardRepository standardRepository;

    public QualityStandardService(QualityStandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    @Transactional(readOnly = true)
    public List<QualityStandardResponse> getStandards() {
        List<QualityStandardResponse> responses = new ArrayList<>();
        for (QualityStandard standard : standardRepository.findAll(Sort.by("id"))) {
            responses.add(toResponse(standard));
        }
        return responses;
    }

    // Changes the range of an existing parameter (the four parameters are created by data.sql)
    @Transactional
    public QualityStandardResponse updateStandard(QualityStandardRequest request) {
        if (request.getMinValue() > request.getMaxValue()) {
            throw new IllegalArgumentException("minValue must not be greater than maxValue");
        }
        QualityStandard standard = standardRepository.findByParameter(request.getParameter())
                .orElseThrow(() -> new ResourceNotFoundException("No standard found for " + request.getParameter()));
        standard.setMinValue(request.getMinValue());
        standard.setMaxValue(request.getMaxValue());
        return toResponse(standardRepository.save(standard));
    }

    private QualityStandardResponse toResponse(QualityStandard standard) {
        QualityStandardResponse response = new QualityStandardResponse();
        response.setId(standard.getId());
        response.setParameter(standard.getParameter());
        response.setMinValue(standard.getMinValue());
        response.setMaxValue(standard.getMaxValue());
        return response;
    }
}
