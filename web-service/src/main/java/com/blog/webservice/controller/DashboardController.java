package com.blog.webservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            username = "User";
        }

        model.addAttribute("username", username);

        return "dashboard";
    }
}