package com.whale.blog.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
    private String nickname;
    private String password;

    public UpdateDto(){}
}
