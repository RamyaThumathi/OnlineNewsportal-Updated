package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // ✅ Encrypts passwords before saving to the database
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authenticates login credentials
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ Main Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity (optional for form-based login)
            .csrf(csrf -> csrf.disable())

            // Authorization rules (who can access what)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/reporter/**").hasRole("REPORTER")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            )

            // Login page configuration
            .formLogin(form -> form
                .loginPage("/login")                   // custom login page
                .defaultSuccessUrl("/default", true)   // redirect after successful login
                .failureUrl("/login?error=true")       // show error on wrong credentials
                .permitAll()
            )

            // ✅ Logout configuration (added logoutUrl)
            .logout(logout -> logout
                .logoutUrl("/logout")                   // URL triggered by logout button
                .logoutSuccessUrl("/login?logout=true") // Redirect after logout
                .permitAll()
            );

        return http.build();
    }
}
