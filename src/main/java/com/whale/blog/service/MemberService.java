package com.whale.blog.service;

import com.whale.blog.domain.Member;
import com.whale.blog.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 데이터 변경이 일어나는 곳에 필수(JPA)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public Long join(Member member) {
        // 중복 아이디 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복 회원 검증 로직
    private void validateDuplicateMember(Member member) {
        memberRepository.findByLoginId(member.getLoginId()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
    }
}
