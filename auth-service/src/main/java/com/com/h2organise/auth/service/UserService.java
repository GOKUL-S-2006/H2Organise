package com.h2organise.auth.service;

import com.h2organise.auth.dto.UserRequest;
import com.h2organise.auth.dto.UserResponse;
import com.h2organise.auth.dto.UserUpdateRequest;
import com.h2organise.auth.entity.Role;
import com.h2organise.auth.entity.User;
import com.h2organise.auth.exception.ConflictException;
import com.h2organise.auth.exception.ResourceNotFoundException;
import com.h2organise.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// user management, mostly used by the admin
// readOnly by default, methods that change data have their own @Transactional
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    // role is an optional filter, e.g. GET /users?role=ENGINEER
    public List<UserResponse> findAll(Role role) {
        List<User> users = role == null ? repository.findAll(Sort.by("id")) : repository.findByRoleOrderByIdAsc(role);
        return users.stream().map(UserResponse::from).toList();
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(getEntity(id));
    }

    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    // admin creates an account with any role
    @Transactional
    public UserResponse create(UserRequest r) {
        return UserResponse.from(createUser(r.fullName(), r.email(), r.password(), r.phone(), r.role(), r.zoneId()));
    }

    // shared by the admin create and the public register (AuthService)
    @Transactional
    public User createUser(String fullName, String email, String password, String phone, Role role, Long zoneId) {
        String cleanEmail = email.trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(cleanEmail)) {
            throw new ConflictException("Email is already registered: " + cleanEmail);
        }
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(cleanEmail);
        // the real password is hashed here and never saved
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(role);
        user.setZoneId(zoneId);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return repository.save(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest r) {
        User user = getEntity(id);
        user.setFullName(r.fullName());
        user.setPhone(r.phone());
        user.setRole(r.role());
        user.setZoneId(r.zoneId());
        // if these are not sent, keep what the user already has
        if (r.active() != null) {
            user.setActive(r.active());
        }
        if (r.newPassword() != null && !r.newPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(r.newPassword()));
        }
        return UserResponse.from(repository.save(user));
    }

    // we don't really delete the user, we only deactivate the account
    // so old complaints that point to this user id still make sense
    @Transactional
    public void deactivate(Long id) {
        User user = getEntity(id);
        user.setActive(false);
        repository.save(user);
    }
}
