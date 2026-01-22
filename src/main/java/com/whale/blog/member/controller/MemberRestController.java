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

    //todo RestController 미구현 상태이므로 구현해야 함


}
