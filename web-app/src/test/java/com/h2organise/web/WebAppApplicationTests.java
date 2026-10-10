package com.h2organise.web;

import com.h2organise.web.client.AuthClient;
import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import com.h2organise.web.client.QualityClient;
import com.h2organise.web.client.ResourceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// the backend services are faked here, we only check that the pages render
@SpringBootTest
@AutoConfigureMockMvc
class WebAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthClient authClient;
    @MockitoBean
    private ResourceClient resourceClient;
    @MockitoBean
    private DistributionClient distributionClient;
    @MockitoBean
    private QualityClient qualityClient;
    @MockitoBean
    private MaintenanceClient maintenanceClient;
    @MockitoBean
    private ComplaintClient complaintClient;

    @BeforeEach
    void fakeData() {
        when(distributionClient.zones()).thenAnswer(i -> list(map("id", 1, "name", "North Zone", "population", 85000, "status", "CRITICAL")));
        when(distributionClient.zone(any())).thenReturn(map("id", 1, "name", "North Zone", "status", "CRITICAL"));
        when(distributionClient.schedules(any())).thenAnswer(i -> list(map("id", 1, "zoneName", "North Zone", "supplyDate", "2026-10-10",
                "startTime", "18:00:00", "endTime", "20:00:00", "plannedVolume", 12.0, "status", "PLANNED")));
        when(distributionClient.consumptionSummary(any())).thenAnswer(i -> list(map("category", "DOMESTIC", "totalVolume", 38.0)));

        when(resourceClient.sources(any())).thenAnswer(i -> list(map("id", 1, "name", "Hemavathi Reservoir", "zoneId", 1,
                "totalCapacity", 500.0, "minThresholdPercent", 30.0)));
        when(resourceClient.latestLevels(any())).thenAnswer(i -> list(map("sourceId", 1, "levelPercent", 22.0, "currentVolume", 110.0)));
        when(resourceClient.source(any())).thenReturn(map("id", 1, "name", "Hemavathi Reservoir"));
        when(resourceClient.capacity(any())).thenReturn(map("levelPercent", 22.0, "totalCapacity", 500.0));

        when(qualityClient.compliance(any())).thenReturn(map("totalTests", 1, "nonCompliantTests", 0, "compliancePercent", 100.0));
        when(qualityClient.latestTest(any())).thenReturn(map("ph", 7.2, "complianceStatus", "COMPLIANT"));

        when(maintenanceClient.faults(any(), any())).thenAnswer(i -> list(map("id", 1, "assetType", "PUMP", "assetId", 3,
                "severity", "HIGH", "status", "OPEN")));
        when(maintenanceClient.tasks(any(), any())).thenAnswer(i -> list(map("id", 1, "title", "Replace Pump P-3 motor",
                "status", "PENDING", "assignedTo", "Suresh")));

        when(complaintClient.complaints(any(), any(), any())).thenAnswer(i -> list(map("id", 101, "type", "LEAKAGE",
                "title", "Pipe burst near market", "priority", "HIGH", "status", "REGISTERED")));
        when(complaintClient.supplyRequests(any(), any(), any())).thenAnswer(i -> list(map("id", 7, "requestType", "TANKER",
                "quantityLitres", 5000, "status", "PENDING")));

        when(authClient.users(any())).thenAnswer(i -> list(map("id", 1, "fullName", "Ravi Kumar", "role", "ENGINEER", "active", true)));
    }

    @Test
    void notLoggedInGoesToLogin() throws Exception {
        mockMvc.perform(get("/dashboard")).andExpect(redirectedUrl("/login"));
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void staffPages() throws Exception {
        MockHttpSession admin = login("ADMIN");
        mockMvc.perform(get("/dashboard").session(admin))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CRITICAL")));
        mockMvc.perform(get("/zones/1/status").session(admin))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Replace Pump P-3 motor")));
        for (String page : List.of("/resources", "/resources/1", "/distribution", "/quality", "/maintenance",
                "/complaints", "/complaints/101", "/admin/users", "/admin/reports")) {
            mockMvc.perform(get(page).session(admin)).andExpect(status().isOk());
        }
    }

    @Test
    void rolesAreChecked() throws Exception {
        mockMvc.perform(get("/dashboard").session(login("CITIZEN"))).andExpect(redirectedUrl("/my/complaints"));
        mockMvc.perform(get("/resources").session(login("OPERATOR"))).andExpect(redirectedUrl("/dashboard"));
        mockMvc.perform(get("/my/complaints").session(login("CITIZEN")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Pipe burst near market")));
    }

    private MockHttpSession login(String role) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", map("id", 2, "fullName", "Test User", "role", role, "zoneId", 1));
        return session;
    }

    // real json from the services has every field (null if empty), so pretend every key exists
    private static Map<String, Object> map(Object... pairs) {
        Map<String, Object> m = new HashMap<>() {
            @Override
            public boolean containsKey(Object key) {
                return true;
            }
        };
        for (int i = 0; i < pairs.length; i += 2) {
            m.put((String) pairs[i], pairs[i + 1]);
        }
        return m;
    }

    @SafeVarargs
    private static List<Map<String, Object>> list(Map<String, Object>... items) {
        return new ArrayList<>(List.of(items));
    }
}
