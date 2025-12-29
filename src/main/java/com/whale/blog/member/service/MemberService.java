package com.whale.blog.member.service;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.dto.JoinDto;
import com.whale.blog.member.dto.UpdateDto;
import com.whale.blog.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // 데이터 변경이 일어나는 곳에 필수(JPA)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 가입
    public Long join(JoinDto joinDto) {
        // 중복 아이디 검증
        validateDuplicateMember(joinDto);
        Member member = new Member(joinDto.getLoginId(), passwordEncoder.encode(joinDto.getPassword()), joinDto.getNickname());
        memberRepository.save(member);
        return member.getId();
    }

    // 중복 회원 검증 로직
    private void validateDuplicateMember(JoinDto joinDto) {
        memberRepository.findByLoginId(joinDto.getLoginId()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        });
    }

    // 회원 조회
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    // 수정
    @Transactional
    public void updateMember(String loginId, UpdateDto updateDto) {
        // 1. 로그인한 사용자 찾기
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("회원정보가 없습니다"));

        // 2. 닉네임 변경
        member.setNickname(updateDto.getNickname());

        // 3. 비밀번호 변경
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            String encodedPwd = passwordEncoder.encode(updateDto.getPassword());
            member.setPassword(encodedPwd);
        }
    }

    // 삭제
    @Transactional
    public void deleteMember(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("회원정보가 없습니다"));

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다");
        }

        //삭제
        memberRepository.delete(member);
    }
}
