package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ComplaintClient extends ApiClient {

    public ComplaintClient(RestTemplate rest, @Value("${services.complaint.url}") String url) {
        super(rest, url);
    }

    public List<Map<String, Object>> complaints(Long zoneId, String status, Long citizenId) {
        return getList("/complaints" + query("zoneId", zoneId, "status", status, "citizenId", citizenId));
    }

    public void addComplaint(Map<String, Object> form) {
        post("/complaints", form);
    }

    // this also adds a row to the status history
    // assignedTo is needed by the service when the status is ASSIGNED
    public void complaintStatus(Long id, String status, String comment, String assignedTo, String updatedBy) {
        Map<String, Object> body = clean(Map.of("status", status,
                "comment", comment == null ? "" : comment,
                "assignedTo", assignedTo == null ? "" : assignedTo,
                "updatedBy", updatedBy));
        put("/complaints/" + id + "/status", body);
    }

    public List<Map<String, Object>> updates(Long complaintId) {
        return getList("/complaints/" + complaintId + "/updates");
    }

    public List<Map<String, Object>> supplyRequests(Long zoneId, String status, Long citizenId) {
        return getList("/supply-requests" + query("zoneId", zoneId, "status", status, "citizenId", citizenId));
    }

    public void addSupplyRequest(Map<String, Object> form) {
        post("/supply-requests", form);
    }

    public void requestStatus(Long id, String status) {
        put("/supply-requests/" + id + "/status", Map.of("status", status));
    }
}
