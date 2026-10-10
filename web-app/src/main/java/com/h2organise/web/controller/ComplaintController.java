package com.h2organise.web.controller;

import com.h2organise.web.client.ComplaintClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// engineer / admin side of complaints and supply requests
@Controller
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintClient complaintClient;

    @GetMapping
    public String list(@RequestParam(name = "status", required = false) String status, Model model) {
        model.addAttribute("complaints", complaintClient.complaints(null, status, null));
        model.addAttribute("requests", complaintClient.supplyRequests(null, null, null));
        model.addAttribute("status", status);
        return "complaints/list";
    }

    @GetMapping("/{id}")
    public String history(@PathVariable("id") Long id, Model model) {
        model.addAttribute("complaintId", id);
        model.addAttribute("updates", complaintClient.updates(id));
        return "complaints/history";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status,
                               @RequestParam(name = "comment", required = false) String comment,
                               @RequestParam(name = "assignedTo", required = false) String assignedTo,
                               HttpSession session) {
        Map<?, ?> user = (Map<?, ?>) session.getAttribute("user");
        complaintClient.complaintStatus(id, status, comment, assignedTo, (String) user.get("fullName"));
        return "redirect:/complaints";
    }

    @PostMapping("/requests/{id}/status")
    public String requestStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        complaintClient.requestStatus(id, status);
        return "redirect:/complaints";
    }
}
