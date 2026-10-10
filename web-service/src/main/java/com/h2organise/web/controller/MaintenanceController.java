package com.h2organise.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MaintenanceController {

    @GetMapping("/maintenance")
    public String maintenance(Model model) {

        List<MaintenanceItem> items = new ArrayList<>();

        items.add(new MaintenanceItem(
                3,
                "Pump P-3",
                "PUMP",
                "North Zone",
                "FAULTY",
                "HIGH",
                "Replace Pump P-3 motor",
                "Suresh",
                "PENDING"
        ));

        items.add(new MaintenanceItem(
                1,
                "North Main Line",
                "PIPELINE",
                "North Zone",
                "LEAKING",
                "MEDIUM",
                "Repair pipeline leak",
                "Maintenance Team",
                "IN_PROGRESS"
        ));

        items.add(new MaintenanceItem(
                1,
                "North Treatment Plant",
                "PLANT",
                "North Zone",
                "OPERATIONAL",
                "LOW",
                "Routine inspection",
                "Suresh",
                "COMPLETED"
        ));

        model.addAttribute("items", items);

        return "maintenance/list";
    }

    public static class MaintenanceItem {

        private int id;
        private String assetName;
        private String assetType;
        private String zone;
        private String assetStatus;
        private String severity;
        private String task;
        private String assignedTo;
        private String taskStatus;

        public MaintenanceItem(
                int id,
                String assetName,
                String assetType,
                String zone,
                String assetStatus,
                String severity,
                String task,
                String assignedTo,
                String taskStatus) {

            this.id = id;
            this.assetName = assetName;
            this.assetType = assetType;
            this.zone = zone;
            this.assetStatus = assetStatus;
            this.severity = severity;
            this.task = task;
            this.assignedTo = assignedTo;
            this.taskStatus = taskStatus;
        }

        public int getId() {
            return id;
        }

        public String getAssetName() {
            return assetName;
        }

        public String getAssetType() {
            return assetType;
        }

        public String getZone() {
            return zone;
        }

        public String getAssetStatus() {
            return assetStatus;
        }

        public String getSeverity() {
            return severity;
        }

        public String getTask() {
            return task;
        }

        public String getAssignedTo() {
            return assignedTo;
        }

        public String getTaskStatus() {
            return taskStatus;
        }
    }
}