package com.h2organise.maintenance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MaintenanceServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void pumpFilters() throws Exception {
        create("/pumps", "{ \"name\": \"Pump P-9\", \"zoneId\": 1 }");
        create("/pumps", "{ \"name\": \"Pump P-3\", \"zoneId\": 1, \"status\": \"FAULTY\" }");

        mockMvc.perform(get("/pumps?zoneId=1&status=FAULTY"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Pump P-3"));
        mockMvc.perform(get("/pumps?status=WORKING"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void resolveFault() throws Exception {
        long faultId = create("/faults", "{ \"assetType\": \"PUMP\", \"assetId\": 3, \"zoneId\": 1, \"severity\": \"HIGH\" }");

        mockMvc.perform(get("/faults/" + faultId))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.reportedAt").isNotEmpty());

        mockMvc.perform(put("/faults/" + faultId + "/resolve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"))
                .andExpect(jsonPath("$.resolvedAt").isNotEmpty());
    }

    @Test
    void completeTask() throws Exception {
        long faultId = create("/faults", "{ \"assetType\": \"PUMP\", \"assetId\": 3, \"zoneId\": 1, \"severity\": \"HIGH\" }");
        long taskId = create("/tasks", "{ \"faultId\": " + faultId + ", \"assetType\": \"PUMP\", \"assetId\": 3, \"zoneId\": 1, "
                + "\"title\": \"Replace Pump P-3 motor\", \"taskType\": \"REPAIR\", \"assignedTo\": \"Suresh\" }");

        mockMvc.perform(get("/tasks?zoneId=1&status=PENDING&assignedTo=Suresh"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].faultId").value(faultId));

        mockMvc.perform(put("/tasks/" + taskId + "/complete")
                        .contentType(MediaType.APPLICATION_JSON).content("{ \"cost\": 12500 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.completedDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").value(12500.0));

        // deleting the fault keeps the task but removes the link
        mockMvc.perform(delete("/faults/" + faultId)).andExpect(status().isNoContent());
        mockMvc.perform(get("/tasks/" + taskId)).andExpect(jsonPath("$.faultId").isEmpty());
    }

    @Test
    void errors() throws Exception {
        mockMvc.perform(post("/faults").contentType(MediaType.APPLICATION_JSON).content("{ \"zoneId\": 1 }"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/plants/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Treatment plant not found with id 9999"));
        mockMvc.perform(get("/faults?severity=URGENT"))
                .andExpect(status().isBadRequest());
    }

    private long create(String url, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
}
