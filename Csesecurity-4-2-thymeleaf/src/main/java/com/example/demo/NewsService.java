package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepo;

    // ✅ Fetch all news
    public List<News> getAllNews() {
        return newsRepo.findAll();
    }

    // ✅ Fetch news by status (without pagination, for admin)
    public List<News> getByStatus(String status) {
        return newsRepo.findByStatus(status.toUpperCase());
    }

    // ✅ Fetch paginated news (for user)
    public Page<News> getApprovedNewsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return newsRepo.findByStatus("APPROVED", pageable);
    }

    // ✅ Save or update news
    public News saveNews(News n) {
        return newsRepo.save(n);
    }

    // ✅ Get news by ID
    public News getById(Long id) {
        return newsRepo.findById(id).orElse(null);
    }

    // ✅ Approve news
    public void approveNews(Long id) {
        News news = getById(id);
        if (news != null) {
            news.setStatus("APPROVED");
            newsRepo.save(news);
        }
    }

    // ✅ Reject news
    public void rejectNews(Long id) {
        News news = getById(id);
        if (news != null) {
            news.setStatus("REJECTED");
            newsRepo.save(news);
        }
    }

    // ✅ Get news by reporter (for reporter dashboard)
    public List<News> getNewsByAuthor(String author) {
        return newsRepo.findByAuthor(author);
    }
}
