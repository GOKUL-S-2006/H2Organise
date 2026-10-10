package com.h2organise.auth.repository;

import com.h2organise.auth.entity.Role;
import com.h2organise.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // spring writes the queries from the method names
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<User> findByRoleOrderByIdAsc(Role role);
}
