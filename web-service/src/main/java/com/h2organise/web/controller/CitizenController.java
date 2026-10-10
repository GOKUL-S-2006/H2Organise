package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CitizenController {

    @GetMapping("/citizen/dashboard")
    public String dashboard() {
        return "citizen/dashboard";
    }
    @GetMapping("/my/zone-status")
    public String zoneStatus() {
        return "citizen/zone-status";
    }

    @GetMapping("/my/complaints")
    public String complaints() {
        return "citizen/complaints";
    }
}