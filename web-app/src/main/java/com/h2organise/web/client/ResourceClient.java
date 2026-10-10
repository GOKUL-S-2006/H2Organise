package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ResourceClient extends ApiClient {

    public ResourceClient(RestTemplate rest, @Value("${services.resource.url}") String url) {
        super(rest, url);
    }

    public List<Map<String, Object>> sources(Long zoneId) {
        return getList("/sources" + query("zoneId", zoneId));
    }

    public Map<String, Object> source(Long id) {
        return getOne("/sources/" + id);
    }

    public Map<String, Object> capacity(Long id) {
        return getOne("/sources/" + id + "/capacity");
    }

    public List<Map<String, Object>> lowSources() {
        return getList("/sources/low");
    }

    public void addSource(Map<String, Object> form) {
        post("/sources", form);
    }

    public void deleteSource(Long id) {
        delete("/sources/" + id);
    }

    // latest reading of every source, or only the sources of one zone
    public List<Map<String, Object>> latestLevels(Long zoneId) {
        return getList("/levels/latest" + query("zoneId", zoneId));
    }

    public List<Map<String, Object>> levels(Long sourceId) {
        return getList("/levels" + query("sourceId", sourceId));
    }

    public void addLevel(Map<String, Object> form) {
        post("/levels", form);
    }
}
