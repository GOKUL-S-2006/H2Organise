package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class DistributionClient extends ApiClient {

    public DistributionClient(RestTemplate rest, @Value("${services.distribution.url}") String url) {
        super(rest, url);
    }

    public List<Map<String, Object>> zones() {
        return getList("/zones");
    }

    public Map<String, Object> zone(Long id) {
        return getOne("/zones/" + id);
    }

    public void addZone(Map<String, Object> form) {
        post("/zones", form);
    }

    public List<Map<String, Object>> pipelines() {
        return getList("/pipelines");
    }

    public void addPipeline(Map<String, Object> form) {
        post("/pipelines", form);
    }

    public void pipelineStatus(Long id, String status) {
        changeStatus("/pipelines/" + id, status);
    }

    public List<Map<String, Object>> schedules(Long zoneId) {
        return getList("/schedules" + query("zoneId", zoneId));
    }

    public void addSchedule(Map<String, Object> form) {
        post("/schedules", form);
    }

    public void scheduleStatus(Long id, String status) {
        changeStatus("/schedules/" + id, status);
    }

    // used when water is low: cut the planned volume and mark it REDUCED
    public void reduceSchedule(Long id, Double newVolume) {
        Map<String, Object> schedule = getOne("/schedules/" + id);
        schedule.put("plannedVolume", newVolume);
        schedule.put("status", "REDUCED");
        put("/schedules/" + id, schedule);
    }

    public List<Map<String, Object>> consumption(Long zoneId) {
        return getList("/consumption" + query("zoneId", zoneId));
    }

    public List<Map<String, Object>> consumptionSummary(Long zoneId) {
        return getList("/consumption/summary" + query("zoneId", zoneId));
    }

    public void addConsumption(Map<String, Object> form) {
        post("/consumption", form);
    }
}
