package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.entity.QualityStandard.Parameter;

public record StandardResponse(Long id, Parameter parameter, Double minValue, Double maxValue) {

    public static StandardResponse from(QualityStandard s) {
        return new StandardResponse(s.getId(), s.getParameter(), s.getMinValue(), s.getMaxValue());
    }
}
