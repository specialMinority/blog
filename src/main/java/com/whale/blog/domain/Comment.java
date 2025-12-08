package com.whale.blog.domain;

public class Comment {
    private Long id;
    private Long postId;
    private String content;
    private String author;

    public Comment() {}

    public Comment(Long postId, String content, String author) {
        this.postId = postId;
        this.content = content;
        this.author = author;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
