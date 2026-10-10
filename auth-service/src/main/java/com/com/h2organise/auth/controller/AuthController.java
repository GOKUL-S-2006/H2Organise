package com.h2organise.auth.controller;

import com.h2organise.auth.dto.LoginRequest;
import com.h2organise.auth.dto.LoginResponse;
import com.h2organise.auth.dto.RegisterRequest;
import com.h2organise.auth.dto.UserResponse;
import com.h2organise.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// REST endpoints for /auth
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    // checks email + password, returns id, name, role and zoneId
    // wrong details -> 401
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    // public sign up, always creates a CITIZEN, returns 201 Created
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }
}
