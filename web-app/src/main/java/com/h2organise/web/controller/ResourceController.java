package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.ResourceClient;
import com.h2organise.web.service.ZoneStatusService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceClient resourceClient;
    private final DistributionClient distributionClient;
    private final ZoneStatusService zoneStatusService;

    @GetMapping
    public String list(Model model) {
        List<Map<String, Object>> sources = resourceClient.sources(null);
        zoneStatusService.addLevels(sources, resourceClient.latestLevels(null));
        model.addAttribute("sources", sources);
        model.addAttribute("zones", distributionClient.zones());
        return "resources/list";
    }

    @PostMapping
    public String add(@RequestParam Map<String, String> form) {
        resourceClient.addSource(ApiClient.clean(form));
        return "redirect:/resources";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        resourceClient.deleteSource(id);
        return "redirect:/resources";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        model.addAttribute("source", resourceClient.source(id));
        model.addAttribute("capacity", resourceClient.capacity(id));
        model.addAttribute("readings", resourceClient.levels(id));
        return "resources/details";
    }

    // new level reading, the service works out the percentage
    @PostMapping("/{id}/levels")
    public String addReading(@PathVariable("id") Long id, @RequestParam Map<String, String> form, HttpSession session) {
        Map<String, Object> body = ApiClient.clean(form);
        body.put("sourceId", id);
        body.put("recordedBy", ((Map<?, ?>) session.getAttribute("user")).get("fullName"));
        resourceClient.addLevel(body);
        return "redirect:/resources/" + id;
    }
}
