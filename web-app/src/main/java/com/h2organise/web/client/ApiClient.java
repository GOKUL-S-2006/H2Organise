package com.h2organise.web.client;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// common get/post/put/delete code so every client doesn't repeat it
// we just use maps for the json, the pages read the fields by name
@SuppressWarnings("unchecked")
public abstract class ApiClient {

    protected final RestTemplate rest;
    protected final String baseUrl;

    protected ApiClient(RestTemplate rest, String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    protected List<Map<String, Object>> getList(String path) {
        List<Map<String, Object>> list = rest.getForObject(baseUrl + path, List.class);
        return list == null ? new ArrayList<>() : list;
    }

    protected Map<String, Object> getOne(String path) {
        return rest.getForObject(baseUrl + path, Map.class);
    }

    protected Map<String, Object> post(String path, Object body) {
        return rest.postForObject(baseUrl + path, body, Map.class);
    }

    protected void put(String path, Object body) {
        rest.put(baseUrl + path, body);
    }

    protected void delete(String path) {
        rest.delete(baseUrl + path);
    }

    // PUT needs the whole object, so load it, change the status and send it back
    protected void changeStatus(String path, String status) {
        Map<String, Object> obj = getOne(path);
        obj.put("status", status);
        put(path, obj);
    }

    // builds ?zoneId=1&status=OPEN and skips the empty ones
    protected static String query(Object... pairs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pairs.length; i += 2) {
            Object value = pairs[i + 1];
            if (value == null || value.toString().isBlank()) {
                continue;
            }
            sb.append(sb.length() == 0 ? "?" : "&").append(pairs[i]).append("=").append(value);
        }
        return sb.toString();
    }

    // html forms send "" for empty boxes, the services want those fields left out
    public static Map<String, Object> clean(Map<String, String> form) {
        Map<String, Object> body = new HashMap<>();
        form.forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                body.put(key, value.trim());
            }
        });
        return body;
    }
}
