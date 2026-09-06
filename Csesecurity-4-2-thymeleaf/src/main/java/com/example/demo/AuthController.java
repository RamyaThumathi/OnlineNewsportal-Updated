package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Use selected role from form, default to ROLE_USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("ROLE_USER");
        }

        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Redirect after login based on role
    @GetMapping("/default")
    public String defaultAfterLogin(org.springframework.security.core.Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_REPORTER"))) {
            return "redirect:/reporter/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
}
