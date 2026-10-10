package com.h2organise.quality;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.quality.entity.QualityParameter;
import com.h2organise.quality.entity.QualityStandard;
import com.h2organise.quality.repository.QualityStandardRepository;

// Runs against an in-memory H2 database (see src/test/resources/application.properties).
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QualityServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QualityStandardRepository standardRepository;

    // data.sql does not run in tests, so add the standards here
    @BeforeEach
    void addStandards() {
        addStandard(QualityParameter.PH, 6.5, 8.5);
        addStandard(QualityParameter.TURBIDITY, 0, 5);
        addStandard(QualityParameter.CHLORINE, 0.2, 1.0);
        addStandard(QualityParameter.TDS, 0, 500);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void goodTestIsCompliant() throws Exception {
        String body = """
                { "sourceId": 1, "zoneId": 1, "testDate": "2026-10-10", "testedBy": "Ravi",
                  "ph": 7.2, "turbidity": 2, "chlorine": 0.5, "tds": 320, "bacteriaPresent": false }
                """;
        mockMvc.perform(post("/tests").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.complianceStatus").value("COMPLIANT"));
    }

    @Test
    void outOfRangeTestIsNonCompliant() throws Exception {
        String highPh = """
                { "zoneId": 1, "testDate": "2026-10-10", "ph": 9.1, "tds": 320 }
                """;
        mockMvc.perform(post("/tests").contentType(MediaType.APPLICATION_JSON).content(highPh))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.complianceStatus").value("NON_COMPLIANT"));

        String bacteria = """
                { "zoneId": 1, "testDate": "2026-10-11", "ph": 7.0, "bacteriaPresent": true }
                """;
        mockMvc.perform(post("/tests").contentType(MediaType.APPLICATION_JSON).content(bacteria))
                .andExpect(jsonPath("$.complianceStatus").value("NON_COMPLIANT"));

        mockMvc.perform(get("/tests/latest?zoneId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testDate").value("2026-10-11"));

        mockMvc.perform(get("/compliance/summary?zoneId=1"))
                .andExpect(jsonPath("$.totalTests").value(2))
                .andExpect(jsonPath("$.nonCompliantTests").value(2))
                .andExpect(jsonPath("$.compliancePercent").value(0.0));
    }

    @Test
    void invalidRequestsReturn400And404() throws Exception {
        mockMvc.perform(post("/tests").contentType(MediaType.APPLICATION_JSON).content("{ \"ph\": 7 }"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/tests/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Quality test not found with id 9999"));

        String badRange = """
                { "parameter": "PH", "minValue": 9, "maxValue": 6 }
                """;
        mockMvc.perform(put("/standards").contentType(MediaType.APPLICATION_JSON).content(badRange))
                .andExpect(status().isBadRequest());
    }

    @Test
    void inspectionsCanBeFilteredAndDueListWorks() throws Exception {
        String inspection = """
                { "sourceId": 1, "zoneId": 1, "inspectionDate": "2026-10-05", "inspectorName": "Ravi",
                  "result": "PASSED", "nextDueDate": "%s" }
                """.formatted(java.time.LocalDate.now().plusDays(10));
        mockMvc.perform(post("/inspections").contentType(MediaType.APPLICATION_JSON).content(inspection))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/inspections?zoneId=1&result=PASSED")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/inspections?result=FAILED")).andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/inspections/due")).andExpect(jsonPath("$", hasSize(1)));
    }

    private void addStandard(QualityParameter parameter, double min, double max) {
        QualityStandard standard = new QualityStandard();
        standard.setParameter(parameter);
        standard.setMinValue(min);
        standard.setMaxValue(max);
        standardRepository.save(standard);
    }
}
