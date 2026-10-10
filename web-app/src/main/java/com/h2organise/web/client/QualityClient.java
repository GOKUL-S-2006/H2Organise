package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class QualityClient extends ApiClient {

    public QualityClient(RestTemplate rest, @Value("${services.quality.url}") String url) {
        super(rest, url);
    }

    public List<Map<String, Object>> standards() {
        return getList("/standards");
    }

    public void updateStandard(Map<String, Object> form) {
        put("/standards", form);
    }

    public List<Map<String, Object>> tests(Long zoneId) {
        return getList("/tests" + query("zoneId", zoneId));
    }

    public void addTest(Map<String, Object> form) {
        post("/tests", form);
    }

    public void deleteTest(Long id) {
        delete("/tests/" + id);
    }

    // a zone with no tests gives 404, we just show nothing then
    public Map<String, Object> latestTest(Long zoneId) {
        try {
            return getOne("/tests/latest" + query("zoneId", zoneId));
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public List<Map<String, Object>> inspections() {
        return getList("/inspections");
    }

    public List<Map<String, Object>> dueInspections() {
        return getList("/inspections/due");
    }

    public void addInspection(Map<String, Object> form) {
        post("/inspections", form);
    }

    public Map<String, Object> compliance(Long zoneId) {
        return getOne("/compliance/summary" + query("zoneId", zoneId));
    }
}
