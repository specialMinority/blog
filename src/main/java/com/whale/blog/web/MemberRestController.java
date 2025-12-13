package com.whale.blog.web;

import com.whale.blog.domain.Member;
import com.whale.blog.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 API
    @PostMapping
    public Member signup(@RequestBody Member member) {
        Long id = memberService.join(member);
        return memberService.findOne(id); // 가입된 정보 반환
    }

    // 회원 조회 API
    @GetMapping("/{id}")
    public Member member(@PathVariable Long id) {
        return memberService.findOne(id);
    }

    // 전체 회원 조회 API
    @GetMapping
    public List<Member> list() {
        return memberService.findMembers();
    }
}
