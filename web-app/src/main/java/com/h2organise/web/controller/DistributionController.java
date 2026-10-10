package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.DistributionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/distribution")
@RequiredArgsConstructor
public class DistributionController {

    private final DistributionClient distributionClient;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("zones", distributionClient.zones());
        model.addAttribute("schedules", distributionClient.schedules(null));
        model.addAttribute("pipelines", distributionClient.pipelines());
        model.addAttribute("consumption", distributionClient.consumption(null));
        model.addAttribute("summary", distributionClient.consumptionSummary(null));
        return "distribution/index";
    }

    @PostMapping("/zones")
    public String addZone(@RequestParam Map<String, String> form) {
        distributionClient.addZone(ApiClient.clean(form));
        return "redirect:/distribution";
    }

    @PostMapping("/schedules")
    public String addSchedule(@RequestParam Map<String, String> form) {
        distributionClient.addSchedule(ApiClient.clean(form));
        return "redirect:/distribution";
    }

    @PostMapping("/schedules/{id}/status")
    public String scheduleStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        distributionClient.scheduleStatus(id, status);
        return "redirect:/distribution";
    }

    @PostMapping("/pipelines")
    public String addPipeline(@RequestParam Map<String, String> form) {
        distributionClient.addPipeline(ApiClient.clean(form));
        return "redirect:/distribution";
    }

    @PostMapping("/pipelines/{id}/status")
    public String pipelineStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        distributionClient.pipelineStatus(id, status);
        return "redirect:/distribution";
    }

    @PostMapping("/consumption")
    public String addConsumption(@RequestParam Map<String, String> form) {
        distributionClient.addConsumption(ApiClient.clean(form));
        return "redirect:/distribution";
    }
}
