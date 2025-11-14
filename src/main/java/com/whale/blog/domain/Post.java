package com.whale.blog.domain;

// 게시글 데이터를 담을 객체 (POJO)
public class Post {
    private Long id;
    private String title;
    private String content;
    private String author;

    // 기본 생성자
    public Post() {}

    // 필드를 받는 생성자
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // --- Getter와 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}