package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QualityController {

    @GetMapping("/quality")
    public String quality(Model model) {

        model.addAttribute("zoneName", "North Zone");
        model.addAttribute("ph", 7.2);
        model.addAttribute("chlorine", 0.6);
        model.addAttribute("qualityStatus", "COMPLIANT");
        model.addAttribute("inspectionStatus", "PASSED");
        model.addAttribute("lastTest", "10 Oct 2026");

        return "quality/list";
    }
}