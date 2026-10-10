package com.h2organise.web.controller;

import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import com.h2organise.web.client.QualityClient;
import com.h2organise.web.client.ResourceClient;
import com.h2organise.web.service.ZoneStatusService;
import com.h2organise.web.service.ZoneStatusView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ResourceClient resourceClient;
    private final DistributionClient distributionClient;
    private final MaintenanceClient maintenanceClient;
    private final ComplaintClient complaintClient;
    private final QualityClient qualityClient;
    private final ZoneStatusService zoneStatusService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Map<String, Object>> zones = distributionClient.zones();
        List<Map<String, Object>> sources = resourceClient.sources(null);
        zoneStatusService.addLevels(sources, resourceClient.latestLevels(null));

        // work out the colour of every zone with the same rule as the zone page
        for (Map<String, Object> zone : zones) {
            List<Map<String, Object>> zoneSources = sources.stream()
                    .filter(s -> String.valueOf(zone.get("id")).equals(String.valueOf(s.get("zoneId"))))
                    .toList();
            ZoneStatusView view = new ZoneStatusView();
            view.setZone(zone);
            zoneStatusService.applyStatusRule(view, zoneSources);
            zone.put("liveStatus", view.getStatus());
            zone.put("lowestLevel", view.getLowestLevel());
        }

        model.addAttribute("zones", zones);
        model.addAttribute("lowSources", resourceClient.lowSources());
        model.addAttribute("openFaults", maintenanceClient.faults(null, "OPEN").size());
        model.addAttribute("pendingTasks", maintenanceClient.tasks(null, "PENDING").size());
        model.addAttribute("newComplaints", complaintClient.complaints(null, "REGISTERED", null).size());
        model.addAttribute("pendingRequests", complaintClient.supplyRequests(null, "PENDING", null).size());
        model.addAttribute("compliance", qualityClient.compliance(null));
        model.addAttribute("usage", distributionClient.consumptionSummary(null));
        return "dashboard";
    }
}
