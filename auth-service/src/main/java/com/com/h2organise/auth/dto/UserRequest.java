package com.h2organise.auth.dto;

import com.h2organise.auth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// body of POST /users, the admin uses this to create engineers, operators and other accounts
public record UserRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @Size(max = 15) String phone,
        @NotNull Role role,
        Long zoneId) {
}
