package com.h2organise.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AuthClient extends ApiClient {

    public AuthClient(RestTemplate rest, @Value("${services.auth.url}") String url) {
        super(rest, url);
    }

    // returns id, fullName, role, zoneId
    public Map<String, Object> login(String email, String password) {
        return post("/auth/login", Map.of("email", email, "password", password));
    }

    public void register(Map<String, Object> form) {
        post("/auth/register", form);
    }

    public List<Map<String, Object>> users(String role) {
        return getList("/users" + query("role", role));
    }

    // admin makes engineer / operator accounts
    public void addUser(Map<String, Object> form) {
        post("/users", form);
    }

    public void changeRole(Long id, String role) {
        Map<String, Object> user = getOne("/users/" + id);
        user.put("role", role);
        put("/users/" + id, user);
    }

    public void deactivate(Long id) {
        delete("/users/" + id);
    }
}
