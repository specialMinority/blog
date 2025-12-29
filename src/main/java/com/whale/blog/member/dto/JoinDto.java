package com.whale.blog.member.dto;

import jakarta.validation.constraints.NotBlank;

public class JoinDto {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    public JoinDto() {} // 가입용 DTO

    // Getter
    public String getLoginId() { return loginId; }

    public String getPassword() { return password; }

    public String getNickname() { return nickname; }
}
