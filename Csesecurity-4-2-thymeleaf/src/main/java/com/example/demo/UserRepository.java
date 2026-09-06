package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // Count how many users have a specific role
    long countByRolesContaining(String role);

    // List users by role
    List<User> findByRolesContaining(String role);
}