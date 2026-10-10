package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceClient maintenanceClient;
    private final DistributionClient distributionClient;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("pumps", maintenanceClient.pumps(null));
        model.addAttribute("plants", maintenanceClient.plants(null));
        model.addAttribute("faults", maintenanceClient.faults(null, null));
        model.addAttribute("tasks", maintenanceClient.tasks(null, null));
        model.addAttribute("zones", distributionClient.zones());
        return "maintenance/index";
    }

    @PostMapping("/pumps")
    public String addPump(@RequestParam Map<String, String> form) {
        maintenanceClient.addPump(ApiClient.clean(form));
        return "redirect:/maintenance";
    }

    @PostMapping("/pumps/{id}/status")
    public String pumpStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        maintenanceClient.pumpStatus(id, status);
        return "redirect:/maintenance";
    }

    @PostMapping("/plants")
    public String addPlant(@RequestParam Map<String, String> form) {
        maintenanceClient.addPlant(ApiClient.clean(form));
        return "redirect:/maintenance";
    }

    @PostMapping("/faults")
    public String addFault(@RequestParam Map<String, String> form) {
        maintenanceClient.addFault(ApiClient.clean(form));
        return "redirect:/maintenance";
    }

    @PostMapping("/faults/{id}/status")
    public String faultStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        maintenanceClient.faultStatus(id, status);
        return "redirect:/maintenance";
    }

    @PostMapping("/faults/{id}/resolve")
    public String resolveFault(@PathVariable("id") Long id) {
        maintenanceClient.resolveFault(id);
        return "redirect:/maintenance";
    }

    @PostMapping("/tasks")
    public String addTask(@RequestParam Map<String, String> form) {
        maintenanceClient.addTask(ApiClient.clean(form));
        return "redirect:/maintenance";
    }

    // repair done: complete the task, resolve its fault and if it was a pump set it back to WORKING
    @PostMapping("/tasks/{id}/complete")
    public String completeTask(@PathVariable("id") Long id) {
        Map<String, Object> task = maintenanceClient.completeTask(id);
        if (task.get("faultId") != null) {
            maintenanceClient.resolveFault(((Number) task.get("faultId")).longValue());
        }
        if ("PUMP".equals(task.get("assetType")) && "REPAIR".equals(task.get("taskType"))) {
            maintenanceClient.pumpStatus(((Number) task.get("assetId")).longValue(), "WORKING");
        }
        return "redirect:/maintenance";
    }
}
