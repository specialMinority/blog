package com.whale.blog.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import com.whale.blog.member.domain.Member;
import com.whale.blog.member.dto.DeleteDto;
import com.whale.blog.member.dto.JoinDto;
import com.whale.blog.member.dto.UpdateDto;
import com.whale.blog.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("users")

public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지 주기
    @GetMapping("signup")
    public String signup() {
        return "users/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@RequestBody JoinDto joinDto) {
        try {
            memberService.join(joinDto);
            return ResponseEntity.ok("가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그인 폼 주기
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    // 마이페이지 주기(GET)
    @GetMapping("/my")
    public String getMyPage(Principal principal, Model model) {
        // Principal: 현재 로그인 중인 사용자의 인증 정보가 담김

        if(principal != null) {
            System.out.println("로그인 유저 = " + principal.getName());
        } else {
            return "redirect:/login"; // 로그인 안 됐으면 로그인 페이지로
        }

        // 1. 로그인한 아이디 가져오기
        String loginId = principal.getName();

        // 2. DB 조회

        Member member = memberService.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없음"));

        model.addAttribute("member", member);

        return "users/my";
    }

    // 수정폼 주기
    @GetMapping("/edit")
    public String editPage(Principal principal, Model model) {

        String loginId = principal.getName();

        Optional<Member> member = memberService.findByLoginId(loginId);

        if(member.isPresent()) {
            model.addAttribute("member", member.get());
        }

        return "users/edit";
    }

    // 수정 처리
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> updateMember(Principal principal,@RequestBody UpdateDto updateDto) {
        try{
            memberService.updateMember(principal.getName(), updateDto);
            return ResponseEntity.ok("수정 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 탈퇴 처리
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMember(Principal principal, @RequestBody DeleteDto deleteDto, HttpServletRequest request) {
        try{
            // 탈퇴 처리
            memberService.deleteMember(principal.getName(), deleteDto.getPassword());

            // 로그아웃 처리
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 세션 삭제
            }

            SecurityContextHolder.clearContext(); // 시큐리티 인증 정보 삭제
            return ResponseEntity.ok("탈퇴 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호가 틀렸읍니다");
        }
    }
}
