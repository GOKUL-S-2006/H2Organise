package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ComplaintController {

    @GetMapping("/complaints")
    public String complaints(Model model) {

        List<Complaint> complaints = new ArrayList<>();

        complaints.add(new Complaint(
                101,
                "LEAKAGE",
                "Pipe burst near market",
                "Market Road",
                "HIGH",
                "REGISTERED"
        ));

        List<SupplyRequest> requests = new ArrayList<>();

        requests.add(new SupplyRequest(
                7,
                "TANKER",
                5000,
                "North Zone",
                "PENDING"
        ));

        model.addAttribute("complaints", complaints);
        model.addAttribute("requests", requests);

        return "complaints/list";
    }

    public static class Complaint {

        private int id;
        private String type;
        private String title;
        private String location;
        private String priority;
        private String status;

        public Complaint(int id, String type, String title,
                         String location, String priority, String status) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.location = location;
            this.priority = priority;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getLocation() {
            return location;
        }

        public String getPriority() {
            return priority;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class SupplyRequest {

        private int id;
        private String type;
        private int quantity;
        private String zone;
        private String status;

        public SupplyRequest(int id, String type, int quantity,
                             String zone, String status) {
            this.id = id;
            this.type = type;
            this.quantity = quantity;
            this.zone = zone;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getZone() {
            return zone;
        }

        public String getStatus() {
            return status;
        }
    }
}