package com.whale.blog.web;

import com.whale.blog.domain.Member;
import com.whale.blog.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @PostMapping("/signup")
    public String signup(@RequestParam String loginId, @RequestParam String password, @RequestParam String nickname) {
        // 1. 받아온 데이터로 Member 객체 생성
        Member member = new Member(loginId, password, nickname);

        // 2. 서비스에 가입 요청
        memberService.join(member);

        // 3. 가입 성공 시 메인 페이지(또는 로그인 페이지)로 이동
        return "redirect:/";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "users/signup";
    }
}
