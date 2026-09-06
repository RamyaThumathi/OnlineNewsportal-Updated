package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reporter")
public class ReporterController {

    @Autowired
    private NewsRepository newsRepo;

    // 🏠 Dashboard – list all news of this reporter
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        String username = auth.getName();
        List<News> newsList = newsRepo.findByAuthor(username);
        model.addAttribute("newsList", newsList);
        return "reporter/reporter-dashboard";
    }

    // 📰 Upload form
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("news", new News());
        return "reporter/uploadNews";
    }

    // 📥 Handle upload
    @PostMapping("/upload")
    public String uploadNews(@ModelAttribute News news,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Authentication auth,
                             Model model) throws IOException {

        if (news.getTitle() == null || news.getTitle().isEmpty() ||
            news.getContent() == null || news.getContent().isEmpty() ||
            news.getNewsType() == null || news.getNewsType().isEmpty()) {

            model.addAttribute("error", "Title, content, and news type are required!");
            return "reporter/uploadNews";
        }

        news.setAuthor(auth.getName());
        news.setStatus("PENDING");

        if (imageFile != null && !imageFile.isEmpty()) {
            news.setImage(imageFile.getBytes());
            news.setImageName(imageFile.getOriginalFilename());
        }

        newsRepo.save(news);
        return "redirect:/reporter/dashboard";
    }

    // 🖼 Display news image
    @GetMapping("/news-image/{id}")
    @ResponseBody
    public byte[] getNewsImage(@PathVariable Long id) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        return news.getImage() != null ? news.getImage() : new byte[0];
    }

 // ✏️ Edit News
    @GetMapping("/edit-news/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        model.addAttribute("news", news);
        return "reporter/reporter-editNews";  // exact HTML file name
    }

    // 💾 Update News
    @PostMapping("/update-news/{id}")
    public String updateNews(@PathVariable Long id,
                             @ModelAttribute("news") News updatedNews,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
            throws IOException {

        News existingNews = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        existingNews.setTitle(updatedNews.getTitle());
        existingNews.setContent(updatedNews.getContent());
        existingNews.setNewsType(updatedNews.getNewsType());
        existingNews.setStatus("PENDING");

        if (imageFile != null && !imageFile.isEmpty()) {
            existingNews.setImage(imageFile.getBytes());
            existingNews.setImageName(imageFile.getOriginalFilename());
        }

        newsRepo.save(existingNews);
        return "redirect:/reporter/dashboard";
    }

    //  Delete News
    @GetMapping("/delete-news/{id}")
    public String showDeleteConfirmation(@PathVariable Long id, Model model) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        model.addAttribute("news", news);
        return "reporter/reporter-deleteNews";
    }

    

    //  Perform Delete
    @PostMapping("/delete-news/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsRepo.deleteById(id);
        return "redirect:/reporter/dashboard";
    }

    // 🚫 View rejected news
    @GetMapping("/rejected-news")
    public String viewRejectedNews(Model model, Authentication auth) {
        String username = auth.getName();
        List<News> rejectedNews = newsRepo.findByAuthorAndStatus(username, "REJECTED");
        model.addAttribute("rejectedNews", rejectedNews);
        return "reporter/reporter-rejectedNews";
    }

    // 🔁 Resubmit rejected news (status -> PENDING)
    @PostMapping("/resubmit-news/{id}")
    public String resubmitNews(@PathVariable Long id) {
        News news = newsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        news.setStatus("PENDING");
        newsRepo.save(news);
        return "redirect:/reporter/rejected-news";
    }
}
