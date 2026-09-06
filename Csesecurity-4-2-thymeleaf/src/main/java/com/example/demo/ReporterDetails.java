package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name="reporter_details")
public class ReporterDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username; // must exist
    private String fullName;
    private String email;
    private String phone;

    // ===== Getters and Setters =====
    public Integer getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
