package com.whale.blog.comment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //JPA가 관리하는 테이블임을 선언
public class Comment {

    @Id // 기본 키 (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    private Long postId; // 어떤 게시글의 댓글인지(PK)
    private String content; // 댓글 내용
    private String author; // 댓글 작성자

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
