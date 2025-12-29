package com.whale.blog.member.controller;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.dto.JoinDto;
import com.whale.blog.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    //todo 수정, 삭제 기능 만들기

    // 회원가입 API

    // 회원 조회 API
}
