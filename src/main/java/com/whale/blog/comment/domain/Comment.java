package com.whale.blog.comment.domain;

import com.whale.blog.post.domain.Post;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA가 관리하는 테이블임을 선언
public class Comment {

    @Id // 기본 키 (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 어떤 게시글의 댓글인지
    //todo Post - Comment 연관관계 매핑

    private String content; // 댓글 내용

    private String author; // 댓글 작성자

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment() {}

    public Comment(Post post, String content, String author, Comment parent) {
        this.post = post;
        this.content = content;
        this.author = author;
        this.parent = parent;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Comment getParent() {
        return parent;
    }
    public void setParent(Comment parent) {
        this.parent = parent;
    }
    public List<Comment> getChildren() {
        return children;
    }
    public void setChildren(List<Comment> children) {
        this.children = children;
    }
}
