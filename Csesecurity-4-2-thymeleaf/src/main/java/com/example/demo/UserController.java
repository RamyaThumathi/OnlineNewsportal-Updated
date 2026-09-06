package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private NewsRepository newsRepo;

    // ✅ USER DASHBOARD — Search + Next/Previous + One News per Page
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Long id,
            Model model) {

        News current = null;

        // ✅ 1 — SEARCH FUNCTIONALITY
        if (keyword != null && !keyword.isEmpty()) {

            Page<News> result = newsRepo.findByTitleContainingIgnoreCaseAndStatus(
                    keyword, "APPROVED", PageRequest.of(0, 1));

            if (result.hasContent()) {
                current = result.getContent().get(0);  // Show first matching news
            } else {
                model.addAttribute("error", "No news found with that title!");
                current = newsRepo.findFirstByStatusOrderByIdAsc("APPROVED");
            }
        }

        // ✅ 2 — Next/Previous news navigation
        else if (id != 0) {
            current = newsRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("News not found"));
        }

        // ✅ 3 — Default (first load)
        else {
            current = newsRepo.findFirstByStatusOrderByIdAsc("APPROVED");
        }

        // ✅ GET NEXT & PREVIOUS NEWS
        News next = newsRepo.findFirstByIdGreaterThanAndStatusOrderByIdAsc(
                current.getId(), "APPROVED");

        News prev = newsRepo.findFirstByIdLessThanAndStatusOrderByIdDesc(
                current.getId(), "APPROVED");

        // ✅ SEND TO UI
        model.addAttribute("news", current);
        model.addAttribute("next", next);
        model.addAttribute("prev", prev);
        model.addAttribute("keyword", keyword);

        return "user/user-dashboard";
    }

    // ✅ Serve Image
    @GetMapping("/news/image/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        News news = newsRepo.findById(id).orElse(null);
        return (news != null && news.getImage() != null) ? news.getImage() : null;
    }
}
