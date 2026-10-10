package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityParameter;
import com.h2organise.quality.entity.QualityStandard;

public record QualityStandardResponse(
        Long id,
        QualityParameter parameter,
        Double minValue,
        Double maxValue) {

    public static QualityStandardResponse from(QualityStandard s) {
        return new QualityStandardResponse(s.getId(), s.getParameter(), s.getMinValue(), s.getMaxValue());
    }
}
