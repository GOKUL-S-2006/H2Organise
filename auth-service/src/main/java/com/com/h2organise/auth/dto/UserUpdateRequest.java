package com.h2organise.auth.dto;

import com.h2organise.auth.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// body of PUT /users/{id}, the email can't be changed
public record UserUpdateRequest(
        @NotBlank @Size(max = 100) String fullName,
        @Size(max = 15) String phone,
        @NotNull Role role,
        Long zoneId,
        Boolean active,                        // optional, keeps the old value if missing
        @Size(min = 6, max = 100) String newPassword) {   // optional, only sent when changing the password
}
