package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private String newsType;

    private String author;

    @Lob
    private byte[] image;

    private String imageName;

    private String status = "PENDING";  // default value

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getNewsType() { return newsType; }
    public void setNewsType(String newsType) { this.newsType = newsType; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
