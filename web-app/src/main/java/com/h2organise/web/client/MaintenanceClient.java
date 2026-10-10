package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class MaintenanceClient extends ApiClient {

    public MaintenanceClient(RestTemplate rest, @Value("${services.maintenance.url}") String url) {
        super(rest, url);
    }

    public List<Map<String, Object>> pumps(Long zoneId) {
        return getList("/pumps" + query("zoneId", zoneId));
    }

    public void addPump(Map<String, Object> form) {
        post("/pumps", form);
    }

    public void pumpStatus(Long id, String status) {
        changeStatus("/pumps/" + id, status);
    }

    public List<Map<String, Object>> plants(Long zoneId) {
        return getList("/plants" + query("zoneId", zoneId));
    }

    public void addPlant(Map<String, Object> form) {
        post("/plants", form);
    }

    public List<Map<String, Object>> faults(Long zoneId, String status) {
        return getList("/faults" + query("zoneId", zoneId, "status", status));
    }

    public Map<String, Object> fault(Long id) {
        return getOne("/faults/" + id);
    }

    public void addFault(Map<String, Object> form) {
        post("/faults", form);
    }

    public void faultStatus(Long id, String status) {
        changeStatus("/faults/" + id, status);
    }

    public void resolveFault(Long id) {
        put("/faults/" + id + "/resolve", null);
    }

    public List<Map<String, Object>> tasks(Long zoneId, String status) {
        return getList("/tasks" + query("zoneId", zoneId, "status", status));
    }

    public Map<String, Object> addTask(Map<String, Object> form) {
        return post("/tasks", form);
    }

    public Map<String, Object> completeTask(Long id) {
        put("/tasks/" + id + "/complete", null);
        return getOne("/tasks/" + id);
    }
}
