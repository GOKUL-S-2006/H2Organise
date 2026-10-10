package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.AuthClient;
import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import com.h2organise.web.client.QualityClient;
import com.h2organise.web.client.ResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthClient authClient;
    private final ResourceClient resourceClient;
    private final DistributionClient distributionClient;
    private final QualityClient qualityClient;
    private final MaintenanceClient maintenanceClient;
    private final ComplaintClient complaintClient;

    @GetMapping("/users")
    public String users(@RequestParam(name = "role", required = false) String role, Model model) {
        model.addAttribute("users", authClient.users(role));
        model.addAttribute("role", role);
        return "admin/users";
    }

    @PostMapping("/users")
    public String addUser(@RequestParam Map<String, String> form) {
        authClient.addUser(ApiClient.clean(form));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable("id") Long id, @RequestParam("role") String role) {
        authClient.changeRole(id, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivate(@PathVariable("id") Long id) {
        authClient.deactivate(id);
        return "redirect:/admin/users";
    }

    // one page with the numbers from every service
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("sources", resourceClient.sources(null).size());
        model.addAttribute("lowSources", resourceClient.lowSources());
        model.addAttribute("zones", distributionClient.zones());
        model.addAttribute("usage", distributionClient.consumptionSummary(null));
        model.addAttribute("compliance", qualityClient.compliance(null));
        model.addAttribute("faults", maintenanceClient.faults(null, null));
        model.addAttribute("tasks", maintenanceClient.tasks(null, null));
        model.addAttribute("complaints", complaintClient.complaints(null, null, null));
        model.addAttribute("requests", complaintClient.supplyRequests(null, null, null));
        return "admin/reports";
    }
}
