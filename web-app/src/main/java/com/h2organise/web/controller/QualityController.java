package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.QualityClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityClient qualityClient;
    private final DistributionClient distributionClient;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("standards", qualityClient.standards());
        model.addAttribute("tests", qualityClient.tests(null));
        model.addAttribute("inspections", qualityClient.inspections());
        model.addAttribute("due", qualityClient.dueInspections());
        model.addAttribute("compliance", qualityClient.compliance(null));
        model.addAttribute("zones", distributionClient.zones());
        return "quality/index";
    }

    @PostMapping("/standards")
    public String updateStandard(@RequestParam Map<String, String> form) {
        qualityClient.updateStandard(ApiClient.clean(form));
        return "redirect:/quality";
    }

    // compliance is decided by quality-service, we only send the readings
    @PostMapping("/tests")
    public String addTest(@RequestParam Map<String, String> form, HttpSession session) {
        Map<String, Object> body = ApiClient.clean(form);
        body.put("testedBy", ((Map<?, ?>) session.getAttribute("user")).get("fullName"));
        body.put("bacteriaPresent", form.containsKey("bacteriaPresent"));
        qualityClient.addTest(body);
        return "redirect:/quality";
    }

    @PostMapping("/tests/{id}/delete")
    public String deleteTest(@PathVariable("id") Long id) {
        qualityClient.deleteTest(id);
        return "redirect:/quality";
    }

    @PostMapping("/inspections")
    public String addInspection(@RequestParam Map<String, String> form) {
        qualityClient.addInspection(ApiClient.clean(form));
        return "redirect:/quality";
    }
}
