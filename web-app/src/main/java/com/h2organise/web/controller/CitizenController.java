package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// pages for citizens: report a leak / problem, ask for water, track what they sent
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class CitizenController {

    private final ComplaintClient complaintClient;
    private final DistributionClient distributionClient;

    @GetMapping("/complaints")
    public String myPage(HttpSession session, Model model) {
        Long citizenId = userId(session);
        model.addAttribute("complaints", complaintClient.complaints(null, null, citizenId));
        model.addAttribute("requests", complaintClient.supplyRequests(null, null, citizenId));
        model.addAttribute("zones", distributionClient.zones());
        return "my/complaints";
    }

    @GetMapping("/complaints/{id}")
    public String history(@PathVariable("id") Long id, Model model) {
        model.addAttribute("complaintId", id);
        model.addAttribute("updates", complaintClient.updates(id));
        return "complaints/history";
    }

    @PostMapping("/complaints")
    public String addComplaint(@RequestParam Map<String, String> form, HttpSession session) {
        Map<String, Object> body = ApiClient.clean(form);
        body.put("citizenId", userId(session));
        complaintClient.addComplaint(body);
        return "redirect:/my/complaints";
    }

    @PostMapping("/requests")
    public String addRequest(@RequestParam Map<String, String> form, HttpSession session) {
        Map<String, Object> body = ApiClient.clean(form);
        body.put("citizenId", userId(session));
        complaintClient.addSupplyRequest(body);
        return "redirect:/my/complaints";
    }

    private Long userId(HttpSession session) {
        Map<?, ?> user = (Map<?, ?>) session.getAttribute("user");
        return ((Number) user.get("id")).longValue();
    }
}
