package com.h2organise.web.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

// everything the zone status page shows, collected from all the services
@Getter
@Setter
public class ZoneStatusView {

    private Map<String, Object> zone;
    private String status;
    private Double lowestLevel;
    private Double threshold;

    private List<Map<String, Object>> sources;
    private List<Map<String, Object>> schedules;
    private List<Map<String, Object>> consumption;
    private List<Map<String, Object>> openFaults;
    private List<Map<String, Object>> pendingTasks;
    private List<Map<String, Object>> pendingRequests;
    private List<Map<String, Object>> newComplaints;
    private Map<String, Object> latestTest;
}
