package com.h2organise.quality.service;

import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.entity.QualityStandard.Parameter;
import com.h2organise.quality.entity.QualityTest;
import com.h2organise.quality.entity.QualityTest.ComplianceStatus;
import com.h2organise.quality.repository.QualityStandardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/** Pure unit test: no database needed. */
@ExtendWith(MockitoExtension.class)
class ComplianceEvaluatorTest {

    @Mock
    private QualityStandardRepository standardRepository;

    private ComplianceEvaluator evaluator;

    @BeforeEach
    void setUp() {
        when(standardRepository.findAll()).thenReturn(List.of(
                standard(Parameter.PH, 6.5, 8.5),
                standard(Parameter.TURBIDITY, 0.0, 5.0),
                standard(Parameter.CHLORINE, 0.2, 1.0),
                standard(Parameter.TDS, 0.0, 500.0)));
        evaluator = new ComplianceEvaluator(standardRepository);
    }

    @Test
    void allValuesInsideRangesIsCompliant() {
        assertEquals(ComplianceStatus.COMPLIANT,
                evaluator.evaluate(test(7.2, 1.2, 0.5, 280.0, false)));
    }

    @Test
    void phTooHighIsNonCompliant() {
        QualityTest t = test(9.1, 1.2, 0.5, 280.0, false);
        assertEquals(ComplianceStatus.NON_COMPLIANT, evaluator.evaluate(t));
        assertEquals(1, evaluator.violations(t).size());
    }

    @Test
    void chlorineTooLowIsNonCompliant() {
        assertEquals(ComplianceStatus.NON_COMPLIANT,
                evaluator.evaluate(test(7.2, 1.2, 0.1, 280.0, false)));
    }

    @Test
    void bacteriaAlwaysMeansNonCompliant() {
        assertEquals(ComplianceStatus.NON_COMPLIANT,
                evaluator.evaluate(test(7.2, 1.2, 0.5, 280.0, true)));
    }

    @Test
    void limitsAreInclusive() {
        assertEquals(ComplianceStatus.COMPLIANT,
                evaluator.evaluate(test(6.5, 5.0, 0.2, 500.0, false)));
    }

    @Test
    void valuesThatWereNotMeasuredAreIgnored() {
        QualityTest t = test(7.0, null, null, null, false);
        assertEquals(ComplianceStatus.COMPLIANT, evaluator.evaluate(t));
        assertTrue(evaluator.violations(t).isEmpty());
    }

    private static QualityStandard standard(Parameter p, double min, double max) {
        return QualityStandard.builder().parameter(p).minValue(min).maxValue(max).build();
    }

    private static QualityTest test(Double ph, Double turbidity, Double chlorine, Double tds, boolean bacteria) {
        return QualityTest.builder().ph(ph).turbidity(turbidity).chlorine(chlorine).tds(tds)
                .bacteriaPresent(bacteria).build();
    }
}
