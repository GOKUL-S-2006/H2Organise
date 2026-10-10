package com.h2organise.quality.dto;

import com.h2organise.quality.entity.QualityParameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QualityStandardResponse {

    private Long id;
    private QualityParameter parameter;
    private Double minValue;
    private Double maxValue;
}
