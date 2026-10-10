package com.h2organise.quality.service;

import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.entity.QualityStandard.Parameter;
import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import com.h2organise.quality.repository.QualityStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The "brain" of the service. Compares one test against the current standards.
 * <ul>
 *   <li>A measured value outside [min, max] (limits are inclusive) is a violation.</li>
 *   <li>A value that was not measured (null) is skipped.</li>
 *   <li>bacteriaPresent = true is always a violation.</li>
 *   <li>No violations means COMPLIANT, otherwise NON_COMPLIANT.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ComplianceEvaluator {

    private final QualityStandardRepository standardRepository;

    public ComplianceStatus evaluate(QualityTest test) {
        return violations(test).isEmpty() ? ComplianceStatus.COMPLIANT : ComplianceStatus.NON_COMPLIANT;
    }

    /** Human-readable reasons for a failed test. Empty list means the test is compliant. */
    public List<String> violations(QualityTest test) {
        Map<Parameter, QualityStandard> standards = standardRepository.findAll().stream()
                .collect(Collectors.toMap(QualityStandard::getParameter, Function.identity(), (a, b) -> a));

        List<String> problems = new ArrayList<>();
        check(problems, "pH", test.getPh(), standards.get(Parameter.PH));
        check(problems, "turbidity", test.getTurbidity(), standards.get(Parameter.TURBIDITY));
        check(problems, "chlorine", test.getChlorine(), standards.get(Parameter.CHLORINE));
        check(problems, "TDS", test.getTds(), standards.get(Parameter.TDS));
        if (Boolean.TRUE.equals(test.getBacteriaPresent())) {
            problems.add("bacteria present");
        }
        return problems;
    }

    private void check(List<String> problems, String name, Double value, QualityStandard standard) {
        if (value == null || standard == null) {
            return;
        }
        if (standard.getMinValue() != null && value < standard.getMinValue()) {
            problems.add(name + " " + value + " is below the minimum " + standard.getMinValue());
        }
        if (standard.getMaxValue() != null && value > standard.getMaxValue()) {
            problems.add(name + " " + value + " is above the maximum " + standard.getMaxValue());
        }
    }
}
