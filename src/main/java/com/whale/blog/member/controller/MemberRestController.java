package com.whale.blog.member.controller;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.dto.MemberDto;
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
    @PostMapping
    public Member signup(@RequestBody MemberDto memberDto) {
        Long id = memberService.join(memberDto);
        return memberService.findOne(id); // todo 회원 성공 여부를 알려주기 (id제공은 위험할 수 있음)
    }

    // 회원 조회 API
    @GetMapping("/{id}")
    public Member member(@PathVariable Long id) {
        return memberService.findOne(id);
    }
}
