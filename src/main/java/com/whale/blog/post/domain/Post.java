//package com.whale.blog.domain;
//
//// jakarta.persistence.* 패키지를 사용합니다. (Spring Boot 3 기준)
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Column;
//
//@Entity // 이 클래스가 DB 테이블(post)과 매핑됨을 JPA에 알립니다.
//public class Post {
//
//    @Id // 이 필드가 테이블의 '기본 키(Primary Key)'임을 알립니다.
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID를 자동으로 생성하도록 합니다. (MySQL의 AUTO_INCREMENT)
//    private Long id;
//
//    private String title;
//
//    @Column(columnDefinition = "TEXT") // content 필드를 TEXT 타입으로 지정 (긴 글)
//    private String content;
//
//    private String author;
//
//    // JPA는 기본 생성자를 필요로 합니다.
//    public Post() {}
//
//    public Post(String title, String content, String author) {
//        this.title = title;
//        this.content = content;
//        this.author = author;
//    }
//
//    public Long getId() { return id; }
//
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//
//    public String getContent() { return content; }
//    public void setContent(String content) { this.content = content; }
//
//    public String getAuthor() { return author; }
//    public void setAuthor(String author) { this.author = author; }
//}