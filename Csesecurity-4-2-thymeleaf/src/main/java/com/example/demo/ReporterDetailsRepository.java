package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporterDetailsRepository extends JpaRepository<ReporterDetails, Integer> {
    // Additional query methods if needed
	
}
