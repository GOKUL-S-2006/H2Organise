package com.h2organise.auth.controller;

import com.h2organise.auth.dto.UserRequest;
import com.h2organise.auth.dto.UserResponse;
import com.h2organise.auth.dto.UserUpdateRequest;
import com.h2organise.auth.entity.Role;
import com.h2organise.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for /users (the admin's "Manage Users" screen)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // example: GET /users?role=ENGINEER
    @GetMapping
    public List<UserResponse> list(@RequestParam(name = "role", required = false) Role role) {
        return service.findAll(role);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // admin creates an account with any role, returns 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request) {
        return service.update(id, request);
    }

    // deactivates the account (does not really delete it), returns 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.deactivate(id);
    }
}
