package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String users() {
        return "admin/users";
    }

    @GetMapping("/admin/zones")
    public String zones() {
        return "admin/zones";
    }
}