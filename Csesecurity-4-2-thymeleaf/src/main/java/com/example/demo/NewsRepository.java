package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // Reporter
    List<News> findByAuthor(String author);

    List<News> findByStatus(String status);

    List<News> findByAuthorAndStatus(String author, String status);

    // ✅ SEARCH BAR SUPPORT
    Page<News> findByTitleContainingIgnoreCaseAndStatus(
            String title, String status, Pageable pageable);

    // ✅ PAGINATION SUPPORT (if needed)
    Page<News> findByStatus(String status, Pageable pageable);

    // ✅ SLIDER SUPPORT (Previous/Next)
    News findFirstByStatusOrderByIdAsc(String status);

    News findFirstByIdGreaterThanAndStatusOrderByIdAsc(Long id, String status);

    News findFirstByIdLessThanAndStatusOrderByIdDesc(Long id, String status);
}
