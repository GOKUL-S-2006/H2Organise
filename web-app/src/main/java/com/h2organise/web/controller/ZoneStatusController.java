package com.h2organise.web.controller;

import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import com.h2organise.web.service.ZoneStatusService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// the main page for the demo: everything about one zone + quick actions
@Controller
@RequestMapping("/zones/{zoneId}")
@RequiredArgsConstructor
public class ZoneStatusController {

    private final ZoneStatusService zoneStatusService;
    private final DistributionClient distributionClient;
    private final MaintenanceClient maintenanceClient;
    private final ComplaintClient complaintClient;

    @GetMapping("/status")
    public String status(@PathVariable("zoneId") Long zoneId, Model model) {
        model.addAttribute("v", zoneStatusService.build(zoneId));
        return "zone-status";
    }

    @PostMapping("/reduce")
    public String reduce(@PathVariable("zoneId") Long zoneId,
                         @RequestParam("scheduleId") Long scheduleId,
                         @RequestParam("volume") Double volume) {
        distributionClient.reduceSchedule(scheduleId, volume);
        return "redirect:/zones/" + zoneId + "/status";
    }

    // makes a repair task for the fault and puts the fault IN_PROGRESS
    @PostMapping("/assign-repair")
    public String assignRepair(@PathVariable("zoneId") Long zoneId,
                               @RequestParam("faultId") Long faultId,
                               @RequestParam("title") String title,
                               @RequestParam("assignedTo") String assignedTo) {
        Map<String, Object> fault = maintenanceClient.fault(faultId);

        Map<String, Object> task = new HashMap<>();
        task.put("faultId", faultId);
        task.put("assetType", fault.get("assetType"));
        task.put("assetId", fault.get("assetId"));
        task.put("zoneId", zoneId);
        task.put("title", title);
        task.put("taskType", "REPAIR");
        task.put("assignedTo", assignedTo);
        task.put("scheduledDate", LocalDate.now().toString());
        maintenanceClient.addTask(task);

        maintenanceClient.faultStatus(faultId, "IN_PROGRESS");
        return "redirect:/zones/" + zoneId + "/status";
    }

    @PostMapping("/request-status")
    public String requestStatus(@PathVariable("zoneId") Long zoneId,
                                @RequestParam("requestId") Long requestId,
                                @RequestParam("status") String status) {
        complaintClient.requestStatus(requestId, status);
        return "redirect:/zones/" + zoneId + "/status";
    }

    @PostMapping("/complaint-status")
    public String complaintStatus(@PathVariable("zoneId") Long zoneId,
                                  @RequestParam("complaintId") Long complaintId,
                                  @RequestParam("status") String status,
                                  @RequestParam("assignedTo") String assignedTo,
                                  HttpSession session) {
        Map<?, ?> user = (Map<?, ?>) session.getAttribute("user");
        complaintClient.complaintStatus(complaintId, status, "Assigned from zone status page", assignedTo,
                (String) user.get("fullName"));
        return "redirect:/zones/" + zoneId + "/status";
    }
}
