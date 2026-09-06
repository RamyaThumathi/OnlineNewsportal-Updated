package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register any type of user (user/reporter/admin)
    public User registerUser(User user, String role) {

        // Check if username exists globally
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set role
        user.setRoles(role);

        return userRepository.save(user);
    }

    // ✅ Find by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
