package com.blog.webservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ZoneStatusController {

    @GetMapping("/zones/{id}/status")
    public String zoneStatus(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            username = "User";
        }

        model.addAttribute("username", username);

        // Sample data for frontend development
        model.addAttribute("zoneId", id);
        model.addAttribute("zoneName", "North Zone");

        model.addAttribute("level", 22);
        model.addAttribute("threshold", 30);
        model.addAttribute("status", "CRITICAL");

        model.addAttribute("sourceName", "Hemavathi Reservoir");
        model.addAttribute("capacity", 500);
        model.addAttribute("currentVolume", 110);

        model.addAttribute("pipelineName", "North Main Line");
        model.addAttribute("pipelineStatus", "LEAKING");

        model.addAttribute("pumpName", "Pump P-3");
        model.addAttribute("pumpStatus", "FAULTY");

        model.addAttribute("maintenanceCount", 1);
        model.addAttribute("requestCount", 1);

        model.addAttribute("qualityStatus", "COMPLIANT");
        model.addAttribute("ph", 7.2);

        model.addAttribute("nextSchedule", "18:00 - 20:00");
        model.addAttribute("plannedVolume", 12);

        return "zone-status";
    }
}