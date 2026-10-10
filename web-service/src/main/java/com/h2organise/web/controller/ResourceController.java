package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ResourceController {

    @GetMapping("/resources")
    public String resources(Model model) {

        List<Resource> resources = new ArrayList<>();

        resources.add(new Resource(
                1L,
                "Hemavathi Reservoir",
                "RESERVOIR",
                "North Zone",
                500,
                22,
                "LOW"
        ));

        resources.add(new Resource(
                2L,
                "South Lake",
                "LAKE",
                "South Zone",
                300,
                65,
                "NORMAL"
        ));

        resources.add(new Resource(
                3L,
                "Central Borewell",
                "WELL",
                "Central Zone",
                100,
                48,
                "NORMAL"
        ));

        model.addAttribute("resources", resources);

        return "resources/list";
    }
    @GetMapping("/resources/new")
    public String newResource() {
        return "resources/form";
    }

    @GetMapping("/resources/{id}")
    public String resourceDetails(@PathVariable Long id, Model model) {

        Resource resource;

        if (id == 1) {

            resource = new Resource(
                    1L,
                    "Hemavathi Reservoir",
                    "RESERVOIR",
                    "North Zone",
                    500,
                    22,
                    "LOW"
            );

        } else if (id == 2) {

            resource = new Resource(
                    2L,
                    "South Lake",
                    "LAKE",
                    "South Zone",
                    300,
                    65,
                    "NORMAL"
            );

        } else {

            resource = new Resource(
                    3L,
                    "Central Borewell",
                    "WELL",
                    "Central Zone",
                    100,
                    48,
                    "NORMAL"
            );
        }

        model.addAttribute("resource", resource);

        return "resources/details";
    }

    public static class Resource {

        private Long id;
        private String name;
        private String type;
        private String zone;
        private int capacity;
        private int level;
        private String status;

        public Resource(Long id,
                        String name,
                        String type,
                        String zone,
                        int capacity,
                        int level,
                        String status) {

            this.id = id;
            this.name = name;
            this.type = type;
            this.zone = zone;
            this.capacity = capacity;
            this.level = level;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getZone() {
            return zone;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getLevel() {
            return level;
        }

        public String getStatus() {
            return status;
        }
    }
}