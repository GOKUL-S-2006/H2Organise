package com.h2organise.auth.dto;

import com.h2organise.auth.entity.Role;
import com.h2organise.auth.entity.User;

// what the web-app gets after a successful login, it keeps this in the session
public record LoginResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        Long zoneId) {

    public static LoginResponse from(User u) {
        return new LoginResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.getZoneId());
    }
}
