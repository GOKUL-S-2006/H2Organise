package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DistributionController {

    @GetMapping("/distribution")
    public String distribution(Model model) {

        model.addAttribute("zoneName", "North Zone");
        model.addAttribute("zoneStatus", "CRITICAL");
        model.addAttribute("dailyConsumption", 55);
        model.addAttribute("nextSchedule", "18:00 - 20:00");
        model.addAttribute("plannedVolume", 12);
        model.addAttribute("pipelineName", "North Main Line");
        model.addAttribute("pipelineStatus", "LEAKING");

        return "distribution/list";
    }
}