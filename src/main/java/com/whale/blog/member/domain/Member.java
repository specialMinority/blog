//todo 도메인 별로 repo, ctrl, service 파일 나누기
package com.whale.blog.member.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB의 login_id 컬럼과 매핑됩니다.
    // unique = true : 중복된 아이디 가입 방지
    @Column(name = "login_id", unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    // DB의 created_at 컬럼과 매핑됩니다.
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- 생성자 ---
    public Member() {}

    public Member(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간 저장
    }

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}