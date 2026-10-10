package com.h2organise.web.service;

import com.h2organise.web.client.ComplaintClient;
import com.h2organise.web.client.DistributionClient;
import com.h2organise.web.client.MaintenanceClient;
import com.h2organise.web.client.QualityClient;
import com.h2organise.web.client.ResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoneStatusService {

    private final ResourceClient resourceClient;
    private final DistributionClient distributionClient;
    private final MaintenanceClient maintenanceClient;
    private final ComplaintClient complaintClient;
    private final QualityClient qualityClient;

    public ZoneStatusView build(Long zoneId) {
        ZoneStatusView view = new ZoneStatusView();

        List<Map<String, Object>> sources = resourceClient.sources(zoneId);
        List<Map<String, Object>> levels = resourceClient.latestLevels(zoneId);
        addLevels(sources, levels);
        view.setSources(sources);

        view.setZone(distributionClient.zone(zoneId));
        view.setSchedules(distributionClient.schedules(zoneId));
        view.setConsumption(distributionClient.consumption(zoneId));

        view.setOpenFaults(maintenanceClient.faults(zoneId, "OPEN"));
        view.setPendingTasks(maintenanceClient.tasks(zoneId, "PENDING"));

        view.setPendingRequests(complaintClient.supplyRequests(zoneId, "PENDING", null));
        view.setNewComplaints(complaintClient.complaints(zoneId, "REGISTERED", null));

        view.setLatestTest(qualityClient.latestTest(zoneId));

        applyStatusRule(view, sources);
        return view;
    }

    // puts levelPercent etc. from the latest reading into each source map
    public void addLevels(List<Map<String, Object>> sources, List<Map<String, Object>> levels) {
        for (Map<String, Object> source : sources) {
            for (Map<String, Object> level : levels) {
                if (num(level.get("sourceId")).equals(num(source.get("id")))) {
                    source.put("levelPercent", level.get("levelPercent"));
                    source.put("currentVolume", level.get("currentVolume"));
                    source.put("readingTime", level.get("readingTime"));
                }
            }
        }
    }

    // zone status rule from the blueprint:
    // lowest level below the threshold -> CRITICAL, within 10 points above it -> WARNING, else NORMAL
    public void applyStatusRule(ZoneStatusView view, List<Map<String, Object>> sources) {
        Double lowest = null;
        Double threshold = null;
        for (Map<String, Object> source : sources) {
            if (source.get("levelPercent") == null) {
                continue;
            }
            double level = num(source.get("levelPercent"));
            if (lowest == null || level < lowest) {
                lowest = level;
                threshold = source.get("minThresholdPercent") == null ? 30.0 : num(source.get("minThresholdPercent"));
            }
        }

        view.setLowestLevel(lowest);
        view.setThreshold(threshold);
        if (lowest == null) {
            // no readings yet, just use what distribution-service has
            view.setStatus(view.getZone() == null ? "NORMAL" : (String) view.getZone().get("status"));
        } else if (lowest < threshold) {
            view.setStatus("CRITICAL");
        } else if (lowest < threshold + 10) {
            view.setStatus("WARNING");
        } else {
            view.setStatus("NORMAL");
        }
    }

    // json numbers come back as Integer or Double so convert them
    private static Double num(Object value) {
        return value == null ? null : ((Number) value).doubleValue();
    }
}
