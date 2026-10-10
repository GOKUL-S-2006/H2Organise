package com.h2organise.auth.dto;

import com.h2organise.auth.entity.Role;
import com.h2organise.auth.entity.User;

import java.time.LocalDateTime;

// data we send back for a user, the password hash is never included
public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        Role role,
        Long zoneId,
        boolean active,
        LocalDateTime createdAt) {

    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getRole(),
                u.getZoneId(), u.isActive(), u.getCreatedAt());
    }
}
