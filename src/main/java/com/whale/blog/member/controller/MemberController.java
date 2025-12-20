package com.whale.blog.member.controller;

import com.whale.blog.member.dto.MemberDto;
import com.whale.blog.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("users")

public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지 보기
    @GetMapping("signup")
    public String signup() {
        return "users/signup";
    }

    // 회원가입 처리
    //todo Entity를 컨트롤러 레이어에서 직접 생성하지 말고 DTO이용하도록 수정(보안)
    //todo 개별조회, 수정, 삭제 기능 만들기
    @PostMapping("/signup")
    public String signup(@ModelAttribute MemberDto memberDto) { // ModelAttribute 에노테이션이 html폼데이터를 알아서 dto객체에 넣어줌

        // 1. 서비스에 가입 요청
        memberService.join(memberDto);

        // 2. 가입 성공 시 메인 페이지로 이동
        return "redirect:/posts";
    }
}
