package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NewsService newsService;

    // 🏠 Dashboard – shows summary and pending news
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<News> pendingNews = newsService.getByStatus("Pending");
        List<News> approvedNews = newsService.getByStatus("Approved");
        List<News> rejectedNews = newsService.getByStatus("Rejected");

        model.addAttribute("pendingNews", pendingNews);
        model.addAttribute("approvedCount", approvedNews.size());
        model.addAttribute("pendingCount", pendingNews.size());
        model.addAttribute("rejectedCount", rejectedNews.size());

        return "admin/dashboard";
    }

    // ✅ Approve News
    @PostMapping("/approve/{id}")
    public String approveNews(@PathVariable Long id) {
        newsService.approveNews(id);
        return "redirect:/admin/dashboard";
    }

    // ❌ Reject News
    @PostMapping("/reject/{id}")
    public String rejectNews(@PathVariable Long id) {
        newsService.rejectNews(id);
        return "redirect:/admin/dashboard";
    }

    // 🟢 View Approved News
    @GetMapping("/approved")
    public String approvedNews(Model model) {
        model.addAttribute("approvedNews", newsService.getByStatus("Approved"));
        return "admin/approvedNews";
    }

    // 🔴 View Rejected News
    @GetMapping("/rejected")
    public String rejectedNews(Model model) {
        model.addAttribute("rejectedNews", newsService.getByStatus("Rejected"));
        return "admin/rejectedNews";
    }

    // 🖼 Serve News Image (for dashboard display)
    @GetMapping(value = "/news-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] newsImage(@PathVariable Long id) {
        News news = newsService.getById(id);
        return (news != null && news.getImage() != null) ? news.getImage() : new byte[0];
    }
}
