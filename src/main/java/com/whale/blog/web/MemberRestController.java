package com.whale.blog.web;

import com.whale.blog.service.MemberService;

public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

}
