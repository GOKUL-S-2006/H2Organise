package com.h2organise.quality.service;

import com.h2organise.quality.dto.QualityStandardRequest;
import com.h2organise.quality.dto.QualityStandardResponse;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.QualityStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QualityStandardService {

    private final QualityStandardRepository repository;

    public List<QualityStandardResponse> findAll() {
        return repository.findAll(Sort.by("id")).stream().map(QualityStandardResponse::from).toList();
    }

    // the four standards come from data.sql, here we only change their range
    @Transactional
    public QualityStandardResponse update(QualityStandardRequest request) {
        if (request.minValue() > request.maxValue()) {
            throw new IllegalArgumentException("minValue must not be greater than maxValue");
        }
        QualityStandard standard = repository.findByParameter(request.parameter())
                .orElseThrow(() -> new ResourceNotFoundException("No standard found for " + request.parameter()));
        standard.setMinValue(request.minValue());
        standard.setMaxValue(request.maxValue());
        return QualityStandardResponse.from(repository.save(standard));
    }
}
