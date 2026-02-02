package com.whale.blog.member.controller;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.dto.DeleteDto;
import com.whale.blog.member.dto.JoinDto;
import com.whale.blog.member.dto.UpdateDto;
import com.whale.blog.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * [POST] 회원가입
     * @param joinDto 회원가입 정보 (loginId, password, nickname)
     * @return 성공 메시지
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto) {
        try {
            memberService.join(joinDto);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [GET] 회원정보 조회
     * @param loginId 조회할 회원의 로그인 ID
     * @param principal 현재 로그인한 사용자 정보
     * @return Member 객체
     */
    @GetMapping("/{loginId}")
    public ResponseEntity<?> findByLoginId(@PathVariable String loginId, Principal principal) {
        // 인증 확인
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 본인 정보만 조회 가능
        if (!principal.getName().equals(loginId)) {
            return ResponseEntity.status(403).body("본인의 정보만 조회할 수 있습니다.");
        }

        return memberService.findByLoginId(loginId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * [PUT] 회원정보 수정
     * @param loginId 수정할 회원의 로그인 ID
     * @param updateDto 수정할 정보 (nickname, password)
     * @param principal 현재 로그인한 사용자 정보
     * @return 성공 메시지
     */
    @PutMapping("/{loginId}")
    public ResponseEntity<String> update(@PathVariable String loginId, 
                                        @RequestBody UpdateDto updateDto, 
                                        Principal principal) {
        // 인증 확인
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 본인 정보만 수정 가능
        if (!principal.getName().equals(loginId)) {
            return ResponseEntity.status(403).body("본인의 정보만 수정할 수 있습니다.");
        }

        try {
            memberService.updateMember(loginId, updateDto);
            return ResponseEntity.ok("회원정보가 수정되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [DELETE] 회원 탈퇴
     * @param loginId 탈퇴할 회원의 로그인 ID
     * @param deleteDto 비밀번호 확인 정보
     * @param principal 현재 로그인한 사용자 정보
     * @return 성공 메시지
     */
    @DeleteMapping("/{loginId}")
    public ResponseEntity<String> delete(@PathVariable String loginId, 
                                        @RequestBody DeleteDto deleteDto, 
                                        Principal principal) {
        // 인증 확인
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 본인만 탈퇴 가능
        if (!principal.getName().equals(loginId)) {
            return ResponseEntity.status(403).body("본인만 탈퇴할 수 있습니다.");
        }

        try {
            memberService.deleteMember(loginId, deleteDto.getPassword());
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
