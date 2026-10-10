package com.h2organise.auth.service;

import com.h2organise.auth.dto.LoginRequest;
import com.h2organise.auth.dto.LoginResponse;
import com.h2organise.auth.dto.RegisterRequest;
import com.h2organise.auth.dto.UserResponse;
import com.h2organise.auth.entity.Role;
import com.h2organise.auth.entity.User;
import com.h2organise.auth.exception.UnauthorizedException;
import com.h2organise.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// login and public sign up
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LoginResponse login(LoginRequest request) {
        // same message for "no such email" and "wrong password", so nobody can guess which emails exist
        User user = repository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        if (!user.isActive()) {
            throw new UnauthorizedException("This account is deactivated");
        }
        return LoginResponse.from(user);
    }

    // anyone can sign up, but only as a CITIZEN
    // engineers and operators are created by the admin with POST /users
    @Transactional
    public UserResponse register(RegisterRequest r) {
        User user = userService.createUser(r.fullName(), r.email(), r.password(), r.phone(), Role.CITIZEN, r.zoneId());
        return UserResponse.from(user);
    }
}
